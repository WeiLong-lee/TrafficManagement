package util;
import vo.Road;
import vo.Car;
import vo.Cross;
import vo.Lane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import util.MyComparator;


public class PreprocUtil {
	/**
	 * ---car数据预处理----
	 * @param  传入的car.txt按时间、顺序分类
	 * car数据格式(id,from,to,speed,planTime)，如(10000, 18, 50, 8, 3)，对应数据格式string,cross,cross,int,string.但此处默认读入的都是string
	 * @author Tricia
	 * @version 2019-3-16
	 */
	public static ArrayList<Car> PreCarData(String[] strings,Map<String,String> ansMap,String[] ans){
		//构建一个用于存放car信息的carList.
		ArrayList<Car> carList = new ArrayList<Car>(); 
		int num = strings.length;   //读入数据的总组数
		for(int i=1;i<num;i++) {    //考虑到第一个是#开头的注释信息，应从i=1开始读
			strings[i]= strings[i].replaceAll("\\(|\\)", "");    //去括号
			strings[i]= strings[i].replaceAll(" ", "");    //去空格

			//分数据, tempStr[0]-id, tempStr[1]-from, tempStr[2]-to,tempStr[3]-speed,tempStr[4]-planTime,
			String[] tempStr = strings[i].split(",");
			//预先填一些信息进去
			ans[i]=tempStr[0];
			ansMap.put(tempStr[0],null);
			//根据tempStr中读入的数据实例化car
			int maxVelocity = Integer.valueOf(tempStr[3]);
			Cross from = new Cross(tempStr[1]);
			Cross to = new Cross(tempStr[2]);
			int planTime = Integer.valueOf(tempStr[4]);
			int NextPos = -2;
			Car c = new Car(tempStr[0],from,to,maxVelocity,planTime,NextPos);
			carList.add(c);
		}

		//对carList按照maxVelocity排序,降序
		MyComparator mc = new MyComparator();
		Collections.sort(carList, mc);

		return carList;
	}

	/**
	 * ---road数据预处理---
	 * road数据格式--(id,length,speed,channel,from,to,isDuplex)，（道路id，道路长度，最高限速，车道数目，起始点id，终点id，是否双向）注：1：双向；0：单向
	 * 相应的格式为---string,int,int,int,Cross,Cross,boolean
	 * @param strings
	 * @return roadList   
	 * @author Tricia
	 * @version 2019-3-16
	 */
	public static ArrayList<Road> PreRoadData(String[] strings){
		ArrayList<Road> roadList = new ArrayList<Road>(); 
		int num = strings.length;   //读入数据的总组数
		for(int i=1;i<num;i++) {    //考虑到第一个是#开头的注释信息，应从i=1开始读
			strings[i]= strings[i].replaceAll("\\(|\\)", "");    //去括号
			strings[i]= strings[i].replaceAll(" ", "");    //去空格
			//分数据, tempStr[0]-id, tempStr[1]-length, tempStr[2]-speed, tempStr[3]-channel,tempStr[4]-from,tempStr[5]-to,tempStr[6]-isDuplex
			String[] tempStr = strings[i].split(",");
			//for(String s:tempStr)
			//	s.trim();
			int length = Integer.valueOf(tempStr[1]);
			int speed = Integer.valueOf(tempStr[2]);
			int channel = Integer.valueOf(tempStr[3]);
			Cross from = new Cross(tempStr[4]);
			Cross to = new Cross(tempStr[5]);
			boolean isDuplex = false;
			if(tempStr[6].equals("1")) {
				isDuplex = true;
			}else {
				isDuplex = false;
			}
			Road road = new Road(tempStr[0],length,speed,channel,from,to,isDuplex);
			if(isDuplex==false) {   //如果是单向道路
				for(int j=0;j<road.getLanesNum();j++) {
					Lane lane=new Lane(j,null);
					road.getLanes().add(lane);
				}
			}else {  //如果是双向道路
				for(int j=road.getLanesNum();j<2*road.getLanesNum();j++) {
					Lane lane=new Lane(j,null);
					road.getLanes().add(lane);
				}
			}
			roadList.add(road);
		}
		return roadList;
	}

	/**
	 * ---cross数据处理
	 * cross数据格式--(id,roadId,roadId,roadId,roadId),(路口id,道路id,道路id,道路id,道路id)上-右-下-左 注：-1表示没有该条道路
	 * @param strings
	 * @return crossList
	 * @author Tricia
	 * @version 2019-3-19
	 */

	public static ArrayList<Cross> PreCrossData(String[] strings){
		ArrayList<Cross> crossList = new ArrayList<Cross>(); 
		int num = strings.length;   //读入数据的总组数
		for(int i=1;i<num;i++) {    //考虑到第一个是#开头的注释信息，应从i=1开始读
			strings[i]= strings[i].replaceAll("\\(|\\)", "");    //去括号
			strings[i]= strings[i].replaceAll(" ", "");    //去空格
			//分数据, tempStr[0]-crossId, tempStr[1]-roadId, tempStr[2]-roadId, tempStr[3]-roadId,tempStr[4]-roadId
			String[] tempStr = strings[i].split(","); 
			ArrayList<Road> roadIDList = new ArrayList<Road>();
			Road upRoad =null;
			Road rightRoad =null;
			Road downRoad =null;
			Road leftRoad =null;
			if(tempStr[1]!="-1") {   //RoadID=-1表示没有这条路
				upRoad = new Road(tempStr[1]);
				roadIDList.add(upRoad);
			}else {
				roadIDList.add(upRoad);
			}
			if(tempStr[2]!="-1") {
				rightRoad = new Road(tempStr[2]);
				roadIDList.add(rightRoad);
			}else {
				roadIDList.add(rightRoad);
			}
			if(tempStr[3]!="-1") {
				downRoad = new Road(tempStr[3]);
				roadIDList.add(downRoad);
			}else {
				roadIDList.add(downRoad);
			}
			if(tempStr[4]!="-1") {
				leftRoad = new Road(tempStr[4]);
				roadIDList.add(leftRoad);
			}else {
				roadIDList.add(leftRoad);
			}
			Cross cross = new Cross(tempStr[0],upRoad,rightRoad,downRoad,leftRoad,roadIDList);
			crossList.add(cross);
		}
		return crossList;
	}

}
