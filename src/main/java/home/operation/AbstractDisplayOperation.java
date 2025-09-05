package home.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract sealed class AbstractDisplayOperation implements IOperation
        permits DisplayOperation, DisplayUniqueOperation, DisplayStringOperation {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDisplayOperation.class);

    protected static final String LS = System.lineSeparator();

    @Override
    public void run(Object values) {
        String msg = getFormattedMsg(values);
        display(msg);
    }

    protected abstract String getFormattedMsg(Object unformattedObjMsg);

    private void display(String msg) {
        LOG.info(LS + msg);
    }
}
