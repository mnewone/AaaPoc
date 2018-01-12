package main;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.SendTo;


@Configuration
@EnableJms
public class JmsConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(JmsConfiguration.class);
	
	@Value("${ActiveMQ.default.broker.url}")
	private String DEFAUT_BROKER_URL;
	@Value("${application.control.bus}")
	private String APP_CONTROL_BUS;
	
	private String APP_BUSINESS_QUEUE="AaaPoCApplication_Queue";
	
	@Autowired
	private ConfigurableApplicationContext myContext;
	
	@Autowired
	private JobLauncher myJobLauncher;
	@Autowired
	private JobOperator myJobOperator;
	
	@Autowired
	private BatchConfiguration myBatchConfiguration;
	
	@Bean
	public ActiveMQConnectionFactory myActiveMQConnectionFactory() {
		ActiveMQConnectionFactory acf = new ActiveMQConnectionFactory();
		acf.setBrokerURL(DEFAUT_BROKER_URL);
		return acf;
	}
	
	
	@Bean
	public JmsTemplate myJmsTemplate() {
		JmsTemplate jt = new JmsTemplate();
		jt.setPubSubDomain(true);
		jt.setConnectionFactory(myActiveMQConnectionFactory());
		return jt;
	}
	
	@Bean
	public ActiveMQTopic vcTopic() {
		ActiveMQTopic vct = new ActiveMQTopic(APP_BUSINESS_QUEUE);
		return vct;
	}
	
	@Bean
	public DefaultJmsListenerContainerFactory myJmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory jlcf = new DefaultJmsListenerContainerFactory();
		jlcf.setConnectionFactory(myActiveMQConnectionFactory());
		jlcf.setConcurrency("1-1");
		return jlcf;
	}
	
	@Bean
	public DefaultJmsListenerContainerFactory topicListenerContainerFactory() {
		DefaultJmsListenerContainerFactory jlcf = new DefaultJmsListenerContainerFactory();
		jlcf.setPubSubDomain(true);
		jlcf.setConnectionFactory(myActiveMQConnectionFactory());
		jlcf.setConcurrency("1-1");
		return jlcf;
	}
	
	@JmsListener(containerFactory = "topicListenerContainerFactory",
			destination = "${application.control.bus}")
	@SendTo("${application.contol.response.bus}")
	public void controlMessage(MapMessage message) throws JMSException{
		String[] beans = myContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            System.out.println(bean);
        }
		System.out.println("Control message received");
		System.out.println(stopApp());
		System.out.println("stoping context...");
		myContext.close();
		
	}
	
	@JmsListener(containerFactory = "myJmsListenerContainerFactory",
			destination = "${ActiveMQ.AaaPoC.input.queue}")
	@SendTo("${ActiveMQ.AaaPoC.output.queue}")
	public void businessMessage(MapMessage message) throws JMSException{
		myContext.registerShutdownHook();
		System.out.println("received message: " + message.getString("jobOrder") 
		+ message.getString("tableName"));
		System.out.println("Message received!" + message.getMapNames());
		Enumeration<String> tt = message.getMapNames();
		String thisTableName = message.getString("tableName");
		String thisJobOrder = message.getString("jobOrder");
		while(tt.hasMoreElements()) {
			System.out.println(tt.nextElement());
		}
		
		String[] beans = myContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            System.out.println(bean);
        }
        
		System.out.println("let's start work...");
		
		Map<String,Job> batchJobs = myBatchConfiguration.getBatchJobs();
		JobParametersBuilder myJPB = new JobParametersBuilder();
		myJPB.addString("tableName", thisTableName);
		myJPB.addString("jobOrder", thisJobOrder);
		try {
			JobExecution thisExecution = myJobLauncher.run(batchJobs.get(thisTableName), 
					myJPB.toJobParameters());
			if(thisExecution.getStatus().name().equals("FAILED")) {
				log.info("Job Failed, retrying ...");
				myJobOperator.restart(thisExecution.getId());
			}
			System.out.println("The job is done.");
		}catch(Exception e) {
			e.printStackTrace();
		}
	  }
	private String stopApp() {
		JmsListenerEndpointRegistry myJLEPR = myContext.getBean(JmsListenerEndpointRegistry.class);
		Collection<MessageListenerContainer> containers = myJLEPR.getListenerContainers();
		containers.stream()
		.forEach(cnt->{cnt.stop();System.out.println("Stoping this container");});
		return "Listener stopped..";
	}
}
