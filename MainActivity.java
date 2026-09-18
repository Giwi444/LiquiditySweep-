package com.liquiditysweep.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import android.widget.Toast;
import android.content.Context;
import java.util.Locale;

public class MainActivity extends Activity {
    DashboardView dashboard;
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.rgb(6,12,12));
        getWindow().setNavigationBarColor(Color.rgb(6,12,12));
        dashboard=new DashboardView(this);
        setContentView(dashboard);
    }

    static class DashboardView extends View {
        Paint p=new Paint(Paint.ANTI_ALIAS_FLAG), stroke=new Paint(Paint.ANTI_ALIAS_FLAG);
        int bg=Color.rgb(6,12,12), card=Color.rgb(14,24,22), card2=Color.rgb(18,31,28);
        int green=Color.rgb(48,224,148), white=Color.rgb(242,248,245), muted=Color.rgb(137,159,151);
        int red=Color.rgb(255,86,104), amber=Color.rgb(255,190,70), blue=Color.rgb(86,169,255);
        boolean running=true; float scale=1f, btnY=0;
        RectF r=new RectF();
        DashboardView(Context c){ super(c); stroke.setStyle(Paint.Style.STROKE); setLayerType(View.LAYER_TYPE_SOFTWARE,null); }
        float s(float v){return v*scale;}
        void txt(Canvas c,String t,float x,float y,float size,int color,boolean bold){
            p.setStyle(Paint.Style.FILL); p.setColor(color); p.setTextSize(s(size));
            p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL)); c.drawText(t,s(x),s(y),p);
        }
        void box(Canvas c,float l,float t,float rr,float bb,float rad,int color){p.setStyle(Paint.Style.FILL);p.setColor(color);c.drawRoundRect(new RectF(s(l),s(t),s(rr),s(bb)),s(rad),s(rad),p);}
        void outline(Canvas c,float l,float t,float rr,float bb,float rad,int color){stroke.setColor(color);stroke.setStrokeWidth(s(1.4f));c.drawRoundRect(new RectF(s(l),s(t),s(rr),s(bb)),s(rad),s(rad),stroke);}
        @Override protected void onDraw(Canvas c){
            super.onDraw(c); c.drawColor(bg); float w=getWidth()/scale, h=getHeight()/scale;
            scale=Math.max(.86f, Math.min(1.12f, w/390f)); w=getWidth()/scale; h=getHeight()/scale;
            txt(c,"LIQUIDITY SWEEP",20,35,22,white,true); txt(c,"EA MOBILE DASHBOARD",20,56,10,muted,true);
            box(c,w-112,18,w-20,52,18,running?Color.rgb(12,55,40):Color.rgb(61,25,31));
            p.setColor(running?green:red); c.drawCircle(s(w-95),s(35),s(4.5f),p); txt(c,running?"RUNNING":"PAUSED",w-83,40,11,running?green:red,true);

            box(c,16,73,w-16,176,22,card);
            txt(c,"ACCOUNT BALANCE",31,98,10,muted,true); txt(c,"$1,000.00",31,130,28,white,true);
            txt(c,"Equity",31,153,10,muted,false); txt(c,"$1,012.84",76,153,11,white,true);
            txt(c,"TODAY",w-110,98,10,muted,true); txt(c,"+$12.84",w-110,128,17,green,true); txt(c,"+1.28%",w-110,151,11,green,true);

            float gap=9, cw=(w-32-gap*2)/3, y=191;
            stat(c,16,y,cw,"OPEN TRADES","01",white); stat(c,16+cw+gap,y,cw,"FLOATING P/L","+$12.84",green); stat(c,16+2*(cw+gap),y,cw,"RECOVERY","0 / 2",white);

            y=278; box(c,16,y,w-16,y+78,20,card); txt(c,"BOT CONNECTION",31,y+25,10,muted,true);
            p.setColor(green); c.drawCircle(s(34),s(y+51),s(5),p); txt(c,"ONLINE",46,y+56,17,green,true); txt(c,"Liquidity Sweep v1.08",w-150,y+55,11,muted,false);

            y=372; box(c,16,y,w-16,y+205,22,card);
            txt(c,"OPEN POSITION",31,y+27,10,muted,true); txt(c,"BTCUSD",31,y+58,21,white,true);
            box(c,w-91,y+17,w-31,y+47,13,Color.rgb(11,57,42)); txt(c,"BUY",w-76,y+37,11,green,true);
            line(c,"Entry","104,250.00",31,y+88,white); line(c,"Stop Loss","103,750.00",31,y+119,red); line(c,"Take Profit","105,250.00",31,y+150,green);
            line(c,"Lot","0.01",w/2+3,y+88,white); line(c,"R:R","1 : 2",w/2+3,y+119,white); line(c,"P/L","+$12.84",w/2+3,y+150,green);
            txt(c,"Last signal  •  07:24:18",31,y+184,10,muted,false);

            btnY=Math.min(y+224,h-78);
            button(c,16,btnY,(w-41)/3,running?"PAUSE":"RESUME",running?amber:green);
            button(c,25+(w-41)/3,btnY,(w-41)/3,"CLOSE ALL",red);
            button(c,34+2*(w-41)/3,btnY,(w-41)/3,"REFRESH",blue);
            txt(c,"DEMO MODE  •  MT5 / TELEGRAM COMING NEXT",20,h-18,9,muted,true);
        }
        void stat(Canvas c,float x,float y,float width,String label,String value,int color){box(c,x,y,x+width,y+68,18,card);txt(c,label,x+11,y+21,8.5f,muted,true);txt(c,value,x+11,y+48,15,color,true);}
        void line(Canvas c,String a,String b,float x,float y,int color){txt(c,a,x,y,10,muted,false);txt(c,b,x+82,y,11.5f,color,true);}
        void button(Canvas c,float x,float y,float width,String text,int color){box(c,x,y,x+width,y+52,17,card2);outline(c,x,y,x+width,y+52,17,color);p.setTextSize(s(10.5f));p.setTypeface(Typeface.DEFAULT_BOLD);float tw=p.measureText(text);txt(c,text,x+width/2-tw/(2*scale),y+32,10.5f,color,true);}
        @Override public boolean onTouchEvent(MotionEvent e){
            if(e.getAction()!=MotionEvent.ACTION_UP) return true;
            float x=e.getX()/scale, y=e.getY()/scale, w=getWidth()/scale;
            float bw=(w-41)/3;
            if(y>=btnY && y<=btnY+60){
                if(x<16+bw){running=!running;invalidate();Toast.makeText(getContext(),running?"Bot resumed (demo)":"Bot paused (demo)",Toast.LENGTH_SHORT).show();}
                else if(x<25+2*bw){Toast.makeText(getContext(),"Close All (demo)",Toast.LENGTH_SHORT).show();}
                else {Toast.makeText(getContext(),"Dashboard refreshed",Toast.LENGTH_SHORT).show();}
            }
            return true;
        }
    }
}
