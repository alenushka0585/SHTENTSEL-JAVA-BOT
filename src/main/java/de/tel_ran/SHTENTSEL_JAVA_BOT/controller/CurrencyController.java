package de.tel_ran.SHTENTSEL_JAVA_BOT.controller;

import de.tel_ran.SHTENTSEL_JAVA_BOT.entity.Currency;
import de.tel_ran.SHTENTSEL_JAVA_BOT.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {
    @Autowired
    CurrencyRepository currencyRepository;


}
