/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import micromips.processor.Cycle;

/**
 *
 * @author Nonbr
 */
public class PipelinePanel extends JPanel {

    private ArrayList<MapBlock> animCycles;

    public PipelinePanel() {
        animCycles = new ArrayList<>();

    }

    public void updatePanel(ArrayList<Cycle> cycles) {
        /*cycles = new ArrayList<>();
            
         cycles.add(new Cycle(Cycle.INSTRUCTION_FETCH, 0, 0));

         cycles.add(new Cycle(Cycle.INSTRUCTION_FETCH, 1, 1));
         cycles.add(new Cycle(Cycle.INSTRUCTION_DECODE, 0, 1));

         cycles.add(new Cycle(Cycle.INSTRUCTION_FETCH, 2, 2));
         cycles.add(new Cycle(Cycle.INSTRUCTION_DECODE, 1, 2));
         cycles.add(new Cycle(Cycle.EXECUTION, 0, 2));

         cycles.add(new Cycle(Cycle.STALL, 2, 3));
         cycles.add(new Cycle(Cycle.EXECUTION, 1, 3));
         cycles.add(new Cycle(Cycle.MEMORY_ACCESS, 0, 3));
        
         cycles.add(new Cycle(Cycle.STALL, 2, 4));
         cycles.add(new Cycle(Cycle.MEMORY_ACCESS, 1, 4));
         cycles.add(new Cycle(Cycle.WRITE_BACK, 0, 4));
        
         cycles.add(new Cycle(Cycle.STALL, 2, 5));
         cycles.add(new Cycle(Cycle.WRITE_BACK, 1, 5));
        
         cycles.add(new Cycle(Cycle.INSTRUCTION_FETCH, 3, 6));
         cycles.add(new Cycle(Cycle.INSTRUCTION_DECODE, 2, 6));
        
         cycles.add(new Cycle(Cycle.STALL, 3, 7));
         cycles.add(new Cycle(Cycle.EXECUTION, 2, 7));
        
         cycles.add(new Cycle(Cycle.STALL, 3, 8));
         cycles.add(new Cycle(Cycle.MEMORY_ACCESS, 2, 8));
        
         cycles.add(new Cycle(Cycle.STALL, 3, 9));
         cycles.add(new Cycle(Cycle.WRITE_BACK, 2, 9));
        
         cycles.add(new Cycle(Cycle.INSTRUCTION_DECODE, 3, 10));
        
         cycles.add(new Cycle(Cycle.EXECUTION, 3, 11));
        
         cycles.add(new Cycle(Cycle.MEMORY_ACCESS, 3, 12));
        
         cycles.add(new Cycle(Cycle.WRITE_BACK, 3, 13));*/
        int x, y;
        for (Cycle cycle : cycles) {
            System.out.println("HEY");
            x = cycle.getClockCycleNo();
            y = cycle.getInstructionNo();
            switch (cycle.getCycleString()) {
                case "IF":
                    animCycles.add(new MapBlock(x * 30, y * 30, "#2ecc71", "IF"));
                    //System.out.println("X: " + x);
                    //drawPane.update(x*30, y*30, "#2ecc71");
                    break;
                case "ID":
                    animCycles.add(new MapBlock(x * 30, y * 30, "#3498db", "ID"));
                    //System.out.println("X: " + x);
                    break;
                case "EX":
                    animCycles.add(new MapBlock(x * 30, y * 30, "#f1c40f", "EX"));
                    //System.out.println("X: " + x);
                    break;
                case "MEM":
                    animCycles.add(new MapBlock(x * 30, y * 30, "#e67e22", "MEM"));
                    //System.out.println("X: " + x);
                    break;
                case "WB":
                    animCycles.add(new MapBlock(x * 30, y * 30, "#e74c3c", "WB"));
                    //System.out.println("X: " + x);
                    break;
                case "*":
                    animCycles.add(new MapBlock(x * 30, y * 30, "#8e44ad", " * "));
                    //System.out.println("X: " + x);
                    break;
                default:
                    break;
            }

            if (x * 30 >= 300) {
                //this.setSize(new Dimension(this.getHeight()+50, this.getWidth()));
                //Dimension d = this.getViewport().getViewSize();
                //d.setSize(d.height+500, d.width);

                //this.getViewport().setViewSize(d);
                this.setPreferredSize(new Dimension(this.getHeight() + 500, this.getWidth()));
                this.revalidate();

            }

            if (y * 30 >= 300) {
                //this.setSize(new Dimension(this.getHeight(), this.getWidth()+50));

                //Dimension d = this.getViewport().getViewSize();
                //d.setSize(d.height, d.width+500);
                //this.getViewport().setViewSize(d);
                this.setPreferredSize(new Dimension(this.getHeight(), this.getWidth() + 500));
                this.revalidate();
            }

            //this.updateUI();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        animCycles.stream().forEach((cycle) -> {
            cycle.paint(g2d);
        });
    }
}
