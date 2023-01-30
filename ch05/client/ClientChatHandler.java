import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientChatHandler extends SimpleChannelInboundHandler<String> {


    //print message received
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        System.out.println("Message received: " + msg);
    }


}
