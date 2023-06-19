package app.networking.utils;

import app.networking.rpcprotocol.AppClientRpcReflectionWorker;
import app.services.AppServices;

import java.net.Socket;

public class RpcConcurrentServer extends AbstractConcurrentServer{

    private AppServices server;

    public RpcConcurrentServer(int port, AppServices server) {
        super(port);
        System.out.println("RpcConcurrentServer started");
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        AppClientRpcReflectionWorker worker = new AppClientRpcReflectionWorker(server, client);
        return new Thread(worker);
    }
}
