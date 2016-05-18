package com.bw.luzz.monkeyapplication.command;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
//不在cell中定义的变量为全局变量
public class CellInterpretor extends CommandInterpretor {
    private List<Listener> list = new ArrayList<>();

    @Override
    public String interprete(String command) throws InterruptedException {
        // TODO Auto-generated method stub

        String[] coms = command.split("\n");
        //循环嵌套标志位
        int nestFlag = 0;
        String commandUnity = "";
        String result = "";
        for (int i = 0; i < coms.length; i++) {
            /*//当代码块结束时
			for(Listener l:list){
				l.onCircleBegin();
			}*/
            String temp = coms[i].trim();
            boolean isLackInfo = temp.endsWith(KeyWorld.IF) || temp.equals(KeyWorld.While) || temp.equals(KeyWorld.Switch) || temp.equals(KeyWorld.For);

            if (isLackInfo) {
                throw new RuntimeException("缺少条件");
            }
            if (temp.startsWith(KeyWorld.IF + KeyWorld.Blank)) {
                nestFlag++;
            } else if (temp.startsWith(KeyWorld.Switch + KeyWorld.Blank)) {
                nestFlag++;
            } else if (temp.startsWith(KeyWorld.While + KeyWorld.Blank)) {
                nestFlag++;
            } else if (temp.startsWith(KeyWorld.For + KeyWorld.Blank)) {
                nestFlag++;
            } else if (temp.equals(KeyWorld.End)) {
                nestFlag--;
            }
            if (!commandUnity.equals("")) {
                commandUnity = commandUnity + KeyWorld.NewLine;
            }
            commandUnity = commandUnity + temp;
            if (nestFlag < 0)
                throw new RuntimeException("循环嵌套出错，是否多写了End，或者嵌套出错");
            if (nestFlag == 0) {

            } else {
                if (i == coms.length - 1 && nestFlag > 0) {
                    throw new RuntimeException("循环无法结束，是否缺少End关键字？");
                }
                continue;
            }

			/*//当代码块开始时
			for(Listener l:list){
				l.onCircleEnd();
			}*/
            result = BananaRunner.execute(commandUnity);
			/*//当代码块结束时
			for(Listener l:list){
				l.onCircleEnd();
			}*/
            if (result != null && result.equals(KeyWorld.Break)) {
                return KeyWorld.Break;
            }
            if (result != null && result.equals(KeyWorld.Continue)) {
                return KeyWorld.Continue;
            }

            commandUnity = "";
        }


        //先不支持返回
        return result;
    }

    public void addListener(Listener mlistener) {
        if (!list.contains(mlistener)) {
            list.add(mlistener);
        }

    }

    public void removeListener(Listener mlistener) {
        if (list.contains(mlistener)) {
            list.remove(mlistener);
        }
    }

    private static CellInterpretor mIfInterpretor;

    private CellInterpretor() {
    }

    public static CellInterpretor getInstance() {
        if (mIfInterpretor == null) {
            synchronized (IfInterpretor.class) {
                if (mIfInterpretor == null) {
                    mIfInterpretor = new CellInterpretor();
                }
            }
        }
        return mIfInterpretor;
    }

    /**
     *
     */
    public interface Listener {
        void onCircleBegin();

        void onCircleEnd() throws InterruptedException;
    }

    public static void main(String[] args) {
        String command = "TracePrint:i \n" +
                "Break \n";

        //CellInterpretor.getInstance().interprete(command);
    }

}
