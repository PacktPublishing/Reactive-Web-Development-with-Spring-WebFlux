import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class Client2 {


    public static void main(String[] args) throws Exception {

        String name = "";
        int serverPort = 8080;
        String host = "127.0.0.1";

        //receive the name of the user who joined the chat
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the packt room chat,  please type your name  : ");
        if (scanner.hasNext()) {
            name = scanner.nextLine();
            System.out.println("Welcome, " + name + "   please type your messages here: ");
        }
        //manage the messages received from the boss
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {


                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        /*
                         * Socket/channel communication happens in byte streams. String decoder &
                         * encoder helps conversion between bytes & String.
                         */
                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());

                        // This is our custom client handler which will have logic for chat.
                        p.addLast(new ClientChatHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(host, serverPort).sync();
        //wait for user type the messages

        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            Channel channel = channelFuture.sync().channel();
            channel.writeAndFlush("Message received from [" + name + "]: " + input);
            channel.flush();
        }
        channelFuture.channel().closeFuture().sync();


    }
}
