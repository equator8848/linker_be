package xyz.equator8848.linker.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionalWrapper {
    @Transactional(rollbackFor = Exception.class)
    public <R> R transactionActionWithResult(Supplier<R> action) {
        return action.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public void transactionActionWithoutResult(SimpleAction simpleAction) {
        simpleAction.action();
    }
}
