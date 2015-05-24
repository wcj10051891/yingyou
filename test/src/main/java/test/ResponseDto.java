package test;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class ResponseDto {
	@Protobuf
	private int a;
	
	public ResponseDto() {
		// TODO Auto-generated constructor stub
	}

	public ResponseDto(int a) {
		super();
		this.a = a;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
}
