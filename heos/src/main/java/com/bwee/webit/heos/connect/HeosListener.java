//package com.bwee.webit.heos.connect;
//
//import com.bwee.webit.heos.commands.SystemCommands;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.concurrent.ExecutorService;
//import java.util.function.Supplier;
//
//@Slf4j
//public class HeosListener extends HeosClient {
//
//    private HeosChangeReader changeListener;
//    private final ExecutorService executorService;
//    private final HeosClient heosClient;
//
//    public HeosListener(final Supplier<String> deviceIp,
//                        final HeosClient heosClient,
//                        final ExecutorService executorService) {
//        super(deviceIp, executorService);
//        this.heosClient = heosClient;
//        this.executorService = executorService;
//    }
//
//    public void startListening(final HeosChangeListener listener) {
//        if (isListening()) {
//            return;
//        }
//
//        registerForChanges(listener);
//    }
//
//    public void registerForChanges(final HeosChangeListener listener) {
//        if (!isConnected()) {
//            connect();
//        }
//
//        final Response r = heosClient.execute(SystemCommands.REGISTER_FOR_CHANGE_EVENTS(true));
//        log.info("Register for change events: {}", r);
//        if (r.isSuccess()) {
//            changeListener = new HeosChangeReader(getIn(), getGson(), listener);
//            executorService.submit(changeListener);
//        }
//    }
//
//    public boolean isListening() {
//        return changeListener != null;
//    }
//
//    @Override
//    public HeosClient close() {
//        if (isListening()) {
//            changeListener.stop();
//            changeListener = null;
//            super.close();
//        }
//        return this;
//    }
//}
