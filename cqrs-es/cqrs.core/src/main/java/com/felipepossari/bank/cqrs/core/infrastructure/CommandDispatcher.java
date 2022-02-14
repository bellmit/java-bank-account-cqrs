package com.felipepossari.bank.cqrs.core.infrastructure;

import com.felipepossari.bank.cqrs.core.commands.BaseCommand;
import com.felipepossari.bank.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {

    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);

    void send(BaseCommand command);
}
