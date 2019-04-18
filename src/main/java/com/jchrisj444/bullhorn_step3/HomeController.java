package com.jchrisj444.bullhorn_step3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    MessagepostRepository messagepostRepository;

    @RequestMapping("/")
    public String listMessageposts(Model model){
        model.addAttribute("messageposts", messagepostRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String messagepostForm(Model model) {
        model.addAttribute("messagepost", new Messagepost());
        return "messagepostform";
    }

    @PostMapping("/add")
    public String processActor(@ModelAttribute Messagepost messagepost,
                               @RequestParam("file")MultipartFile file) {
        if(file.isEmpty()){
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    com.cloudinary.utils.ObjectUtils.asMap("resourcetype", "auto"));
            messagepost.setPostpic(uploadResult.get("url").toString());
            messagepostRepository.save(messagepost);
        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }

    @PostMapping("/process")
    public String processForm(@Valid Messagepost message,
                              BindingResult result){
        if (result.hasErrors()){
            return "messagepostform";
        }
        messagepostRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMessagepost(@PathVariable("id") long id, Model model){
        model.addAttribute("messagepost", messagepostRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessagepost(@PathVariable("id") long id,
                                    Model model){
        model.addAttribute("messagepost", messagepostRepository.findById(id).get());
        return "messagepostform";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){
        messagepostRepository.deleteById(id);
        return "redirect:/";
    }
}

