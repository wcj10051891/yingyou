package com.shadowgame.rpg.persist.entity;
/** 任务配置 */
public class TMission{
	/** 任务id */
	public Integer id;
	/** 名称 */
	public String name="";
	/** 接受条件1 */
	public String acceptCond1;
	/** 接受条件2 */
	public String acceptCond2;
	/** 接受条件3 */
	public String acceptCond3;
	/** 接受条件4 */
	public String acceptCond4;
	/** 接受条件5 */
	public String acceptCond5;
	/** 任务目标1 */
	public String goal1;
	/** 任务目标2 */
	public String goal2;
	/** 任务目标3 */
	public String goal3;
	/** 任务目标4 */
	public String goal4;
	/** 任务目标5 */
	public String goal5;
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getAcceptCond1(){
		return this.acceptCond1;
	}
	public String getAcceptCond2(){
		return this.acceptCond2;
	}
	public String getAcceptCond3(){
		return this.acceptCond3;
	}
	public String getAcceptCond4(){
		return this.acceptCond4;
	}
	public String getAcceptCond5(){
		return this.acceptCond5;
	}
	public String getGoal1(){
		return this.goal1;
	}
	public String getGoal2(){
		return this.goal2;
	}
	public String getGoal3(){
		return this.goal3;
	}
	public String getGoal4(){
		return this.goal4;
	}
	public String getGoal5(){
		return this.goal5;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setAcceptCond1(String acceptCond1){
		this.acceptCond1 = acceptCond1;
	}
	public void setAcceptCond2(String acceptCond2){
		this.acceptCond2 = acceptCond2;
	}
	public void setAcceptCond3(String acceptCond3){
		this.acceptCond3 = acceptCond3;
	}
	public void setAcceptCond4(String acceptCond4){
		this.acceptCond4 = acceptCond4;
	}
	public void setAcceptCond5(String acceptCond5){
		this.acceptCond5 = acceptCond5;
	}
	public void setGoal1(String goal1){
		this.goal1 = goal1;
	}
	public void setGoal2(String goal2){
		this.goal2 = goal2;
	}
	public void setGoal3(String goal3){
		this.goal3 = goal3;
	}
	public void setGoal4(String goal4){
		this.goal4 = goal4;
	}
	public void setGoal5(String goal5){
		this.goal5 = goal5;
	}
}

