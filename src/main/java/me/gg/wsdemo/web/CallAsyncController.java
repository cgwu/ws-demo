package me.gg.wsdemo.web;

import lombok.extern.slf4j.Slf4j;
import me.gg.wsdemo.component.CallAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by sam on 18-12-24.
 */
@Controller
@RequestMapping("/async")
@Slf4j
public class CallAsyncController {

    @Autowired
    private CallAsyncService callAsyncService;

    @RequestMapping("/action/{id}")
    @ResponseBody
    public String action(@PathVariable String id) {
        String result = callAsyncService.doAction(id);
        return "Action Done: " + result;
    }

    @RequestMapping("/notify/{id}")
    @ResponseBody
    public String notify(@PathVariable String id, @RequestParam String data) {
        boolean result = callAsyncService.notifyAction(id,data);
        return "Notify Done: " + result;
    }

}
