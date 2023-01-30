import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler extends SimpleChannelInboundHandler<String> {

    static final List<Channel> chatChannels = new ArrayList<Channel>();


    public void userChannelActive(final ChannelHandlerContext channelHandlerContext) {
        System.out.println("New user joined the chat" + channelHandlerContext);
        chatChannels.add(channelHandlerContext.channel());
    }

    //  receive messages from all users
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {
        System.out.println("Message received - " + s);
        for (Channel c : chatChannels) {
            c.writeAndFlush("-> " + s + '\n');
        }
    }


}



