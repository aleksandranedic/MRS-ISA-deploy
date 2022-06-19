package com.project.team9.configuration;

import com.project.team9.model.user.Client;
import com.project.team9.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled",matchIfMissing = true)
public class SchedulingConfiguration {
    public final ClientService clientService;

    @Autowired
    public SchedulingConfiguration(ClientService clientService) {
        this.clientService = clientService;
    }

    @Scheduled(cron = "0 0 0 1 * *")//s m h dayOfMouuth mount dayofWeek ako se stavi /2 kod necega znaci svake dve..
    public void deletePenalties(){
        for (Client client: clientService.getClients()) {
            if(client.getNumOfPenalties()>0){
                client.setNumOfPenalties(0);
                clientService.addClient(client);
            }
        }
    }
}
