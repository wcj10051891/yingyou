package com.shadowgame.rpg.msg;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.shadowgame.rpg.core.NoticeException;
import com.shadowgame.rpg.net.msg.ClientMsg;

/**
 * 登录消息
 * 
 * @author wcj10051891@gmail.com
 * @Date 2015年5月27日 上午10:03:53
 */
@Msg(10000)
public class LoginMsg extends ClientMsg {
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void handleLogin(ChannelHandlerContext ctx) {
//		Services.tcpService.send(this, ctx.getChannel());
		throw new NoticeException("尼玛");
	}
	
}
