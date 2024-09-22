package jaso.log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

public class OneRoundTrip {

	public static void main(String[] args) throws InterruptedException, IOException {

		// start the server
		final HelloWorldServer server = new HelloWorldServer();
		server.start();

		String target = "localhost:50051";
		ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
		try {
			HelloWorldClient client = new HelloWorldClient(channel);
			client.greet("user");
		} finally {
			// ManagedChannels use resources like threads and TCP connections. To prevent
			// leaking these
			// resources the channel should be shut down when it will no longer be used. If
			// it may be used
			// again leave it running.
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}

		server.stop();

	}

}
