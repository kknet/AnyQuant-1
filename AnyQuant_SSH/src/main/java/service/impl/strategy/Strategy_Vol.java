package service.impl.strategy;

import util.MyDate;
import vo.ReportVO;

import java.util.List;

/**
 * Created by 67534 on 2016/5/27.
 */
public class Strategy_Vol extends MultiStockStrategy{


    public Strategy_Vol()
    {
        super();
    }


    public void setPara_Vol(double capital, double taxRate, String baseCode ,
                           MyDate start , MyDate end , int vol, int interval ){

        super.setPara_Mutil(capital,taxRate,baseCode,start,end,vol);
        this.interval=interval;
    }


    /**
     * 初始买入交易日当天交易量最小的vol只股票
     */
    @Override
    public void init() {
        System.out.println("Strategy_VOL init-------");
        this.curTradeDay=start;
        this.buyStocks();
    }

    /**
     * 调仓日首先平仓，再购买
     */
    @Override
    public void handleData() {

        this.sellStocks();

        this.buyStocks();
    }

    @Override
    public ReportVO analyse() {
       return simpleAnalyse();
    }


    /**
     * 买入交易日当天交易量最小的vol只股票
     */
    @Override
    protected void buyStocks() {
        /**
        * 首先清空股票池
        */
        stocks.clear();
        /**
         * 获取市场上当天成交量最小的vol只股票加入股票池
         */
        List<String> codes = stockDataDAO.getStockCodeByVolDec(curTradeDay,vol);
        /**
         * 挑选成交量最小的股票
         */
        int i=0;
        for(String code:codes){
            if(code!=null){
                stocks.add(code);
                //System.out.println(code);
                i++;
            }
        }
        System.out.println("size: "+stocks.size()+"---------------------");

        /**
         * 获取每只股票交易日当天的均价（总交易额/总交易量）
         * 因为可能会出现返回值不足vol个数据，因此先补充0，再赋值
         */
        double [] temp=stockDataDAO.getAvgPriceByCodes(stocks,curTradeDay);
        buy_Prices= new double[vol]; //这里讲买入价格全设为0
        for( i=0;i<temp.length;i++){
            buy_Prices[i]=temp[i];
        }


        /**
         * 确定每只股票买入的手数
         * 并记录花费
         */
        for(i=0;i<stocks.size();i++){
            /**
             * 如果买入价格为0，就忽略该股票
             * 把买入手数设为0
             */
            if(buy_Prices[i]==0){
                lots[i]=0;
            }else{
                lots[i]= (int) (capital/vol/(buy_Prices[i]*stocksPerLot));
            }

            System.out.println("buy "+stocks.get(i)+" "+lots[i]*stocksPerLot+" at price: "+buy_Prices[i]);
            expense+=lots[i]*stocksPerLot*buy_Prices[i];
        }

        /**
         * 记录当日的指数价格
         */
        base_BuyPrice=benchMarkDAO.getAvgPrice(this.baseCode,curTradeDay);
    }


    /**
     * 简单平仓，并计算累计收益率
     */
    @Override
    protected void sellStocks() {
        simpleSellStocks();
    }


}