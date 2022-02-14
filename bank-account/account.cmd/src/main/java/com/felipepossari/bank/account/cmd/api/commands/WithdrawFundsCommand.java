package com.felipepossari.bank.account.cmd.api.commands;

import com.felipepossari.bank.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class WithdrawFundsCommand extends BaseCommand {
    private double amount;
}
