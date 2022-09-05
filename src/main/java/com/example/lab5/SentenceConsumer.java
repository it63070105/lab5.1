package com.example.lab5;

import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {

    protected Sentence sentences = new Sentence();

    @RabbitListener(queues = "BadWordQueue")
    public void addBadSentence(String s){
        sentences.badSentences.add(s);
//        System.out.println("Listen from Rabbit bad : " + s);
        System.out.println("In addBadSentence Method : " + sentences.badSentences);
    }

    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentence(String s){
        sentences.goodSentences.add(s);
//        System.out.println("Listen from Rabbit good : " + s);
        System.out.println("In addGoodSentence Method : " + sentences.goodSentences);
    }
}
