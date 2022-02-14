package com.felipepossari.bank.account.cmd.api.commands;

import com.felipepossari.bank.cqrs.core.commands.BaseCommand;

public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id) {
        super(id);
    }
}
