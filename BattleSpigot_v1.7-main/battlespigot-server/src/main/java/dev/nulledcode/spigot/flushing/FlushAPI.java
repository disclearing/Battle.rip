package dev.nulledcode.spigot.flushing;

public class FlushAPI {
    private static FlushManager api;

    /**
     * Have a direct and easy access to the queued flushing api
     *
     * @return Method to get the return from the api
     */

    public static FlushManager getApi() {
        return api;
    }

    /**
     * Minecraft servers work on a queued flushing basis in order to limit the quantity of synchronized
     * and thread locked packets from overloading the netty pipeline due to the constant flushing. Flushing
     * can quickly become a performance impacting process, hence it is favorable to keep it limited; Furthermore,
     * flushing benefits better server-client synchronization and ensures that *most the time* packets fall under
     * the same tick. This of course can differ from time to time.
     * <p>
     * There is no perfect method but this is very good
     *
     * @param provided Activation of the Flush Manager
     */

    public static void setApi(FlushManager provided) {
        assert api != null : "Flush API already internally registered";
        FlushAPI.api = provided;
    }
}
