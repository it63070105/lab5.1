package com.example.lab5;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WordPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    protected Word words = new Word();

    @RequestMapping(value = "/addBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> addBadWord(@PathVariable("word") String s){
        words.badWords.add(s);
        return words.badWords;
    }

    @RequestMapping(value = "/delBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s){
        int index = words.badWords.indexOf(s);
        words.badWords.remove(index);
        return words.badWords;
    }

    @RequestMapping(value = "/addGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> addGoodWords(@PathVariable("word") String s){
        words.goodWords.add(s);
        return words.goodWords;
    }

    @RequestMapping(value = "/delGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s){
        int index = words.goodWords.indexOf(s);
        words.goodWords.remove(index);
        return words.goodWords;
    }

    @RequestMapping(value = "/proof/{sentence}", method = RequestMethod.GET)
    public String proofSentence(@PathVariable("sentence") String s){
//        System.out.println(s);
//        System.out.println("bad List : " + words.badWords);
//        System.out.println("good List : " + words.goodWords);
//        if (words.badWords.contains(s) && words.goodWords.contains(s)){
//            System.out.println("Found Bad & Good Word");
//        }
//        else if (words.badWords.contains(s) && !words.goodWords.contains(s)){
//            System.out.println("Found Bad Word");
//        }
//        else if (!words.badWords.contains(s) && words.goodWords.contains(s)){
//            System.out.println("Found Good Word");
//        }
        boolean good = false;
        boolean bad = false;
        for (String word: words.badWords){
            if (s.contains(word)){
                bad = true;
                break;
            }
        }
        for (String word: words.goodWords){
            if (s.contains(word)){
                good = true;
                break;
            }
        }
        if (bad && good){
            rabbitTemplate.convertAndSend("Fanout","",s);
            return "Found Bad & Good Word";
        }
        else if (bad && !good){
            rabbitTemplate.convertAndSend("Direct","bad",s);
            return "Found Bad Word";
        }
        else if (!bad && good){
            rabbitTemplate.convertAndSend("Direct","good",s);
            return "Found Good Word";
        }
        return "";
    }
}
