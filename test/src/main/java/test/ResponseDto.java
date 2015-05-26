package test;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class ResponseDto {
	@Protobuf
	private Long a;
	@Protobuf
	private Long b;
	@Protobuf
	private Long c;
	@Protobuf
	private Long d;
	@Protobuf
	private Long e;

	public ResponseDto() {
	}

	public ResponseDto(Long a) {
		super();
		this.a = a;
	}

	public Long getA() {
		return a;
	}

	public void setA(Long a) {
		this.a = a;
	}

	public Long getB() {
		return b;
	}

	public void setB(Long b) {
		this.b = b;
	}

	public Long getC() {
		return c;
	}

	public void setC(Long c) {
		this.c = c;
	}

	public Long getD() {
		return d;
	}

	public void setD(Long d) {
		this.d = d;
	}

	public Long getE() {
		return e;
	}

	public void setE(Long e) {
		this.e = e;
	}

}
