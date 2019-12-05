package com.hendra.transaction.demo.controller;

import com.hendra.transaction.demo.dao.BankAccountDAO;
import com.hendra.transaction.demo.exception.BankTransactionException;
import com.hendra.transaction.demo.form.SendMoneyForm;
import com.hendra.transaction.demo.model.BankAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private BankAccountDAO bankAccountDAO;

    @RequestMapping(method = RequestMethod.GET)
    public String showBankAccounts(Model model) {
        List<BankAccountInfo> list = bankAccountDAO.getBankAccounts();

        model.addAttribute("accountInfos", list);

        return "accountsPage";
    }

    @RequestMapping(value = "sendMoney", method = RequestMethod.GET)
    public String viewSendMoneyPage(Model model) {

        SendMoneyForm form = new SendMoneyForm(1L, 2L, 700d);

        model.addAttribute("sendMoneyForm", form);

        return "sendMoneyPage";
    }

    @RequestMapping(value = "sendMoney", method = RequestMethod.POST)
    public String processSendMoney(Model model, SendMoneyForm sendMoneyForm) {

        System.out.println("Send Money::" + sendMoneyForm.getAmount());

        try {
            bankAccountDAO.sendMoney(sendMoneyForm.getFromAccountId(), //
                    sendMoneyForm.getToAccountId(), //
                    sendMoneyForm.getAmount());
        } catch (BankTransactionException e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "sendMoneyPage";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "saveNewMember", method = RequestMethod.GET)
    public String viewSaveNewMember(Model model) {

        BankAccountInfo form = new BankAccountInfo(999L, "Hendra", 9000);

        model.addAttribute("newMemberForm", form);

        return "saveNewMember";
    }

    @RequestMapping(value = "saveNewMember", method = RequestMethod.POST)
    public String saveNewMember(Model model, BankAccountInfo bankAccountInfo){

        try
        {
            bankAccountDAO.save(bankAccountInfo.getId(), bankAccountInfo.getFullName(), bankAccountInfo.getBalance());
        } catch (Exception e)
        {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "saveNewMember";
        }

        return "redirect:/";
    }
}
