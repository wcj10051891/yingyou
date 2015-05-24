package xgame.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public abstract class RandomUtils {
	private static final Random rand = new Random(System.nanoTime());
	/**
	 * Returns a random float between min and max.
	 * 
	 * @return A random int between <tt>min</tt> (inclusive) to <tt>max</tt>
	 *         (inclusive).       [min, max]
	 */
	public static int nextRandomInt(int min, int max) {
		if (max < min)
            max = min;
        return rand.nextInt(max - min + 1) + min;
	}
	
	public static boolean randomCheck(int min, int max, int target) {
	    if(target >= max)
	        return true;

        target = target * 100;
	    int _min = 1;
	    int _max = max * 100;
	    
	    int r1 = nextRandomInt(_min, _max);
	    int r2 = r1 + target - 1;
	    
	    if(r2 > _max){
	        int temp = r1;
	        r1 = r1 - target + 1;
	        if(r1 < 0){
	            r1 = 1;
	            r2 = r1 + target - 1;
	        }else{
	            r2 = temp;
	        }
	    }
	    int ran = nextRandomInt(_min, _max);
	    return ran >= r1 && ran <= r2;
    }

    /**
     * 计算概率是否命中
     * @param rate    传入一个浮点概率值 [0.0f, 1.0f]
     * @return           是否命中
     */
    public static boolean randomHit(Number rate)
    {
        return randomHit(rate, null);    
    }
    public static boolean randomHit(Number rate, StringBuilder sb)
    {
        if (rate.floatValue() == 0)
            return false;
        float r = rand.nextFloat();
        if (sb != null)
            sb.append(r);
        return  r <= rate.floatValue();
    }

    /**
	 * 计算概率是否命中
	 * 规则，在[1~分母]之间随机抽取，要是抽中的数字落在[1~分子]（包括2端）之间则抽中
	 * @param numerator		分子
	 * @param denominator	分母
	 * @return	抽中true，未中fasle;
	 */
	public static boolean randomHit(int numerator, int denominator){
		if(numerator > denominator)
			throw new IllegalArgumentException("Param 'numerator' must less than param 'denominator'.");
		int target = nextRandomInt(1, denominator);
		if(target <= numerator){
			return true;
		}
		return false;
	}	
	
	/**
	 * 在区间列表中查找目标数字落在哪个区间
	 * @param ranges	区间列表，列表中Integer[] 为区间的2个端点值，
	 * @param find		待查找的值
	 * @return
	 */
	private static Integer[] findRange(Collection<Integer[]> ranges, Integer find){
		for(Iterator<Integer[]> it = ranges.iterator();it.hasNext();){
			Integer[] range = it.next();
			if(find >= range[0] && find <= range[1]){
				return range;
			}
		}
		return null;
	}

	public static <V> RangeData<V> getRangeMap(Map<V, Integer> value2Rates){
        return getRangeMap(value2Rates.values(), value2Rates.keySet());
    }
	
	@SuppressWarnings("unchecked")
    public static <V> RangeData<V> getRangeMap(Collection<Integer> rates, Collection<V> values){
        return (RangeData<V>) getRangeMap(Arrays.asList(rates.toArray(new Integer[rates.size()])), Arrays.asList(values.toArray()));
    }
    
    private static <V> RangeData<V> getRangeMap(List<Integer> rates, List<V> values){
        Map<Integer[], V> ranges = new HashMap<Integer[], V>();
        int last = 0;
        for(int i = 0; i < rates.size(); i++){
            int vv = rates.get(i);
            ranges.put(new Integer[]{last + 1, vv + last}, values.get(i));
            last = vv + last;
        }
        RangeData<V> r = new RangeData<V>();
        r.data = ranges;
        r.max = last;
        return r;
    }
	
	public static class RangeData<V> implements Cloneable{
	    public Map<Integer[], V> data;
	    public int max;
	    @Override
	    public String toString()
	    {
	        if(data == null)
	            return "null";
	        
	        StringBuilder sb = new StringBuilder();
	        for(Entry<Integer[], V> e : data.entrySet())
	        {
	            Integer[] i = e.getKey();
	            sb.append(i[0] + "," + i[1]).append("\n");
	        }
	        return "max:" + max + "->" + sb.toString();
	    }
	    @Override
	    public RangeData<V> clone(){
	        RangeData<V> rt = new RangeData<V>();
	        rt.data = new HashMap<Integer[], V>(this.data);
	        rt.max = max;
            return rt;
	    }
	}
    
    /**
     * 随机挑选出的区间对应的值
     * @param <V>
     * @param ranges
     * @return V
     */
    public static <V> V findRange(RangeData<V> ranges){
        return ranges.data.get(findRange(ranges.data.keySet(), RandomUtils.nextRandomInt(1, ranges.max)));
    }

    /**
     * 找出随机数落在哪个区间
     * @param rates  概率权重
     * @return
     */
    public static int findIndex(List<? extends Number> rates)
    {
        return findIndex(rates.toArray(new Number[rates.size()]));
    }
    public static int findIndex(Number[] rates)
    {
        if (rates.length == 0)
            return -1;
        float sum = 0;
        for (Number f : rates)
            sum += f.floatValue();

        float r = rand.nextFloat() * sum;
        sum = 0;
        for (int i=0; i<rates.length; i++)
        {
            sum += rates[i].floatValue();
            if (r < sum)
                return i;
        }
        if (rates[rates.length-1].floatValue() == 0)
            return -1;
        return rates.length-1;
    }
    public static int getItemIndex(int[] rates)
    {
        int index = -1;
        /**
         * 1. 计算所有权重的总和，并转换成int型值 2. 以所有权重总和做为最大值，随机生成一个权重值 3.找出最小的大于随机权重值所对应的装备ID
         */
        int rateTotalInt = 0; // 权重的总和
        for (int rate : rates)
        {
            rateTotalInt += rate;
        }
        int randomRate = RandomUtils.nextRandomInt( 0, rateTotalInt-1); // 随机一个权重值
        
        for (int i = 0; i < rates.length; i++)
        {
            int rate = rates[i];
            if (randomRate > rate)
            {
                randomRate -= rate;
            }
            else
            {
                index = i;
                break;
            }
        }
        return index;
    }
}
