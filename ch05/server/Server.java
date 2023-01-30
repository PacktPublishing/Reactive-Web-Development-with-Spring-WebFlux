import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public final class Server {
    public static void main(String[] args) throws InterruptedException {

        //port that will run our chat server
        int serverPort = 8080;

        //receive connections from clients
        EventLoopGroup serverGroup = new NioEventLoopGroup(1);

        //handle incoming client connections
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        //base code to start our server
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(serverGroup, clientGroup)
                .channel(NioServerSocketChannel.class)// configure channel NIO to receive connections and messages
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline = socketChannel.pipeline();
                        //encoders and decoders
                        channelPipeline.addLast(new StringDecoder());
                        channelPipeline.addLast(new StringEncoder());

                        // Our custom handlers
                        channelPipeline.addLast(new ClientChatHandler());
                    } //initialize channel

                });

        ChannelFuture channelFuture = serverBootstrap.bind(serverPort).sync();
        System.out.println("Packt chat room started");
        channelFuture.channel().closeFuture().sync();


    }

}