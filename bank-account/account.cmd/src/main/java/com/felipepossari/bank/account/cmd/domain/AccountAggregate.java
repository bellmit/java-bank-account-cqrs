package com.felipepossari.bank.account.cmd.domain;

import com.felipepossari.bank.account.cmd.api.commands.OpenAccountCommand;
import com.felipepossari.bank.account.common.events.AccountClosedEvent;
import com.felipepossari.bank.account.common.events.AccountOpenedEvent;
import com.felipepossari.bank.account.common.events.FundsDepositedEvent;
import com.felipepossari.bank.account.common.events.FundsWithdrawnEvent;
import com.felipepossari.bank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(
                AccountOpenedEvent.builder()
                        .id(command.getId())
                        .accountHolder(command.getAccountHolder())
                        .createdDate(new Date())
                        .accountType(command.getAccountType())
                        .openingBalance(command.getOpeningBalance())
                        .build()
        );
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("Account closed");
        }
        if (amount <= 0) {
            throw new IllegalStateException("Invalid deposit amount");
        }
        raiseEvent(
                FundsDepositedEvent.builder()
                        .id(this.id)
                        .amount(amount)
                        .build()
        );
    }

    public void apply(FundsDepositedEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("Account closed");
        }
        raiseEvent(
                FundsWithdrawnEvent.builder()
                        .id(this.id)
                        .amount(amount)
                        .build()
        );
    }

    public void apply(FundsWithdrawnEvent event) {
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount() {
        if (!this.active) {
            throw new IllegalStateException("Account closed");
        }
        raiseEvent(
                AccountClosedEvent.builder()
                        .id(this.id)
                        .build()
        );
    }

    public void apply(AccountClosedEvent event){
        this.id = event.getId();
        this.active = false;
    }
}
