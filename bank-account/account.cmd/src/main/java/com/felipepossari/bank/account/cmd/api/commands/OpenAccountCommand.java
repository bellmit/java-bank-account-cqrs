package com.felipepossari.bank.account.cmd.api.commands;

import com.felipepossari.bank.account.common.dto.AccountType;
import com.felipepossari.bank.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}
