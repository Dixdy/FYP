package centralisedSystem;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphPlot {
	
	private int plotWidth;
	private int plotHeight;
	private int xMargin;
	private int yMargin;
	private TimeLine timeLine;
	private String[] xAxis;
	
	public GraphPlot(int width, int height, int xMargin, int yMargin) {
        this.plotWidth = width;
        this.plotHeight = height;
        this.xMargin = xMargin;
        this.yMargin = yMargin;
    }
	
	private class TimeLine {
		private List<Event> events;
		
		public TimeLine() {
			this.events = new ArrayList<>();
		}
		
		public int getLastMiliSecond() {
			double latestEndTime = 0;
			for (Event event : events) {
				if (event.getComputingEndTime() > latestEndTime) {
					latestEndTime = event.getComputingEndTime();
				}
			}
			int lastMiliSecond = (int) (latestEndTime * 10);
			return lastMiliSecond;
		}

		public void addEvent(Event event) {
			this.events.add(event);
		}
		
		public List<Event> getEvents(){
			return this.events;
		}
	}

	private class Event {
		private double uploadStartTime;
		private double uploadEndTime;
		private double computingStartTime;
		private double computingEndTime;
		private double arrivalTime;
		private String name;
		
		public Event() {
			
		}
		
		public String getName() {
			return this.name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		public double getUploadStartTime() {
			return uploadStartTime;
		}

		public void setUploadStartTime(double uploadStartTime) {
			this.uploadStartTime = uploadStartTime;
		}

		public double getUploadEndTime() {
			return uploadEndTime;
		}

		public void setUploadEndTime(double uploadEndTime) {
			this.uploadEndTime = uploadEndTime;
		}

		public double getComputingStartTime() {
			return computingStartTime;
		}

		public void setComputingStartTime(double computingStartTime) {
			this.computingStartTime = computingStartTime;
		}

		public double getComputingEndTime() {
			return computingEndTime;
		}

		public void setComputingEndTime(double computingEndTime) {
			this.computingEndTime = computingEndTime;
		}

		public double getArrivalTime() {
			return arrivalTime;
		}

		public void setArrivalTime(double arrivalTime) {
			this.arrivalTime = arrivalTime;
		}
	}

	private class TimeLinePanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private String serverName;
        private int plotWidth;
        private int plotHeight;
        private int x;
        private int y;
        private int xMargin;
        private int yMargin;
        private int tickLength;
        private int canvasHeight;
        private int canvasWidth;
        private int canvasIntervalGapHeight;
        private int canvasIntervalGapWidth;

        private String[] xAxis;
        private TimeLine timeLine;

        public TimeLinePanel(int plotWidth, int plotHeight, int xMargin, int yMargin, TimeLine timeLine, String[] xAxis, String serverName) {
            this.serverName = serverName;
            this.plotWidth = plotWidth;
            this.plotHeight = plotHeight;
            this.xMargin = xMargin;
            this.yMargin = yMargin;
            this.tickLength = 10;
            this.timeLine = timeLine;
            this.xAxis = xAxis;
            this.canvasHeight = this.plotHeight - this.yMargin;
            this.canvasWidth = this.plotWidth - this.xMargin * 2;
            this.canvasIntervalGapHeight = 50;
            this.canvasIntervalGapWidth = canvasWidth/(this.xAxis.length - 1);
            this.setPreferredSize(new Dimension(plotWidth, plotHeight));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setFont(getFont().deriveFont(14f).deriveFont(Font.BOLD));
            drawTitle(g2d);
            drawLegend(g2d);
            drawTaskNames(g2d);
            drawArrivalTimeLines(g2d);
            drawUploadTimeLines(g2d);
            drawComputingTimeLines(g2d);
            drawXAxis(g2d);
        }

        private void drawTitle(Graphics2D g2d) {
        	x = this.canvasWidth/2;
        	y = yMargin;
        	String name = serverName + " Task Schedule";
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke());
        	g2d.drawString(name, x, y);
        	g2d.drawLine(x - 2, y + 2, x + g2d.getFontMetrics(getFont()).stringWidth(name) + 2*name.length(), y + 2);
        }
        
        private void drawLegend(Graphics2D g2d) {
        	x = this.canvasWidth - this.canvasWidth/6;
        	y = yMargin/2;
        	g2d.setStroke(new BasicStroke(10f));
        	g2d.setColor(Color.BLACK);
        	g2d.drawString("Legend:", x + 10, y + 5);
        	x = x + 80;
        	g2d.setColor(Color.RED);
        	g2d.drawLine(x, y, x + 1, y);
        	g2d.setColor(Color.BLACK);
        	g2d.drawString("Arrival Time", x + 10, y + 5);
        	y = y + 20;
        	g2d.setColor(Color.YELLOW);
        	g2d.drawLine(x, y, x + 1, y);
        	g2d.setColor(Color.BLACK);
        	g2d.drawString("Uploading Time", x + 10, y + 5);
        	y = y + 20;
        	g2d.setColor(Color.BLUE);
        	g2d.drawLine(x, y, x + 1, y);
        	g2d.setColor(Color.BLACK);
        	g2d.drawString("Computing Time", x + 10, y + 5);        	
        }
        
        private void drawXAxis(Graphics2D g2d) {
            x = xMargin;
            int xStart = x;

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2f));
            FontMetrics metrics = g2d.getFontMetrics(getFont());
            for (int i = 0; i < xAxis.length; i++) {
            	int labelX = x - metrics.stringWidth(xAxis[i]) / 2;
                int labelY = y + tickLength * 2 + metrics.getHeight() / 2 ;
                g2d.drawLine(x, y, x, y + tickLength);
                g2d.drawString(xAxis[i], labelX, labelY);
                x += canvasIntervalGapWidth;
            }
            g2d.drawLine(xStart, y, x - canvasIntervalGapWidth, y);
        }

        private void drawTaskNames(Graphics2D g2d) {
            List<Event> events = timeLine.getEvents();

            x = xMargin;
            y = yMargin * 2;

            g2d.setStroke(new BasicStroke(10f));
            FontMetrics metrics = g2d.getFontMetrics(getFont());
            for (Event event : events) {
            	String name = event.getName();
            	int labelX = x;
                int labelY = y - metrics.getHeight() / 2 ;
                g2d.setColor(Color.BLACK);
                g2d.drawString(name, labelX, labelY);
                
                y += canvasIntervalGapHeight;
            }
        }
        
        private void drawArrivalTimeLines(Graphics2D g2d) {
            List<Event> events = timeLine.getEvents();
            x = xMargin;
            y = yMargin * 2;

            g2d.setStroke(new BasicStroke(10f));
            for (Event event : events) {
            	double arrivalTime = event.getArrivalTime();
            	int eventStart = x + (int) (((arrivalTime)/0.1) * canvasIntervalGapWidth)-1;
            	int eventEnd = x + (int) (((arrivalTime)/0.1) * canvasIntervalGapWidth);

                g2d.setColor(Color.RED);
                g2d.drawLine(eventStart, y, eventEnd, y);
                y += canvasIntervalGapHeight;
            }
        }
        
        private void drawUploadTimeLines(Graphics2D g2d) {
            List<Event> events = timeLine.getEvents();
            x = xMargin;
            y = yMargin * 2;

            g2d.setStroke(new BasicStroke(10f));
            for (Event event : events) {
            	double uploadStartTime = event.getUploadStartTime();
            	double uploadEndTime = event.getUploadEndTime();
            	int eventStart = x + (int) (((uploadStartTime)/0.1) * canvasIntervalGapWidth);
            	int eventEnd = x + (int) (((uploadEndTime)/0.1) * canvasIntervalGapWidth);

                g2d.setColor(Color.YELLOW);
                g2d.drawLine(eventStart, y, eventEnd, y);
                y += canvasIntervalGapHeight;
            }
        }
        
        private void drawComputingTimeLines(Graphics2D g2d) {
            List<Event> events = timeLine.getEvents();
            x = xMargin;
            y = yMargin * 2;

            g2d.setStroke(new BasicStroke(10f));
            for (Event event : events) {
            	double computingStartTime = event.getComputingStartTime();
            	double computingEndTime = event.getComputingEndTime();
            	int eventStart = x + (int) (((computingStartTime)/0.1) * canvasIntervalGapWidth);
            	int eventEnd = x + (int) (((computingEndTime)/0.1) * canvasIntervalGapWidth);

                g2d.setColor(Color.BLUE);
                g2d.drawLine(eventStart, y, eventEnd, y);
                y += canvasIntervalGapHeight;
            }
        }
    }
	
	private TimeLine generateTimeLine(ArrayList<TaskInfo> serverSeq) {
		TimeLine timeLine = new TimeLine();
		Event event;
		
		for (TaskInfo task : serverSeq) {
			event = new Event();
			event.setArrivalTime(task.getArrivalTime());
			event.setUploadStartTime(task.getUploadStartTime());
			event.setUploadEndTime(task.getUploadStartTime() + task.getUploadLatency());
			event.setComputingStartTime(task.getComputingStartTime());
			event.setComputingEndTime(task.getComputingStartTime() + task.getComputingTime());
			event.setName("Task " + task.getTaskNumber());
			timeLine.addEvent(event);
		}

		return timeLine;
	}
	
	private String[] createXAxis() {
		String[] axis = new String[timeLine.getLastMiliSecond()+2];
		double time = 0;
		for (int i=0; i < axis.length; i++) {
            axis[i] = String.format("%.1f",time);
            time+=0.1;
        }
		return axis;
	}

	public void generateGraph(String string, ArrayList<TaskInfo> serverSeq, String serverName) {
        this.timeLine = generateTimeLine(serverSeq);
        this.xAxis = createXAxis();
        
		JFrame frame = new JFrame(string + " " + serverName + " Task Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TimeLinePanel timeLinePanel = new TimeLinePanel(plotWidth, plotHeight, xMargin, yMargin, timeLine, xAxis, serverName);
        frame.add(timeLinePanel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
	}
}
