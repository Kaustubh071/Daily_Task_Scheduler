import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class Task extends JPanel {

    JLabel index;
    JTextField taskName;
    JButton done;
    
    Color pink = new Color(255, 161, 161);
    Color green = new Color(188, 226, 158);
    Color doneColor = new Color(233, 119, 119);

    private boolean checked;

    Task() {
        this.setPreferredSize(new Dimension(400, 20)); 
        this.setBackground(pink); 

        this.setLayout(new BorderLayout()); 

        checked = false;

        index = new JLabel(""); 
        index.setPreferredSize(new Dimension(20, 20)); 
        index.setHorizontalAlignment(JLabel.CENTER); 
        this.add(index, BorderLayout.WEST); 

        taskName = new JTextField(" "); 
        taskName.setBorder(BorderFactory.createEmptyBorder()); 
        taskName.setBackground(pink); 

        this.add(taskName, BorderLayout.CENTER);

        done = new JButton("Task Done ?");
        done.setPreferredSize(new Dimension(120, 20));
        done.setBorder(BorderFactory.createEmptyBorder());
        done.setBackground(doneColor);
        done.setFocusPainted(false);

        this.add(done, BorderLayout.EAST);

    }

    public void changeIndex(int num) {
        this.index.setText(num + ""); 
        this.revalidate();
    }

    public JButton getDone() {
        return done;
    }

    public boolean getState() {
        return checked;
    }

    public void changeState() {
        this.setBackground(green);
        taskName.setBackground(green);
        checked = true;
        revalidate();
    }
}

class List extends JPanel {

    Color lightColor = new Color(255,255,204);

    List() {

        GridLayout layout = new GridLayout(10, 1);
        layout.setVgap(5); 

        this.setLayout(layout);
        this.setPreferredSize(new Dimension(400, 560));
        this.setBackground(lightColor);
    }

    public void updateNumbers() {
        Component[] listItems = this.getComponents();

        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i] instanceof Task) {
                ((Task) listItems[i]).changeIndex(i + 1);
            }
        }

    }

    public void removeCompletedTasks() {

        for (Component c : getComponents()) {
            if (c instanceof Task) {
                if (((Task) c).getState()) {
                    remove(c); 
                    updateNumbers(); 
                }
            }
        }

    }
}


class Footer extends JPanel {

    JButton addTask;
    JButton clear;
    JButton logout;
    JButton remindTask;
    Color lightblue = new Color(173, 216, 230);
    Color lightColor = new Color(255,255,204);
    Border emptyBorder = BorderFactory.createEmptyBorder();
    List list;
    Footer(List list) {
        this.list = list;
        this.setPreferredSize(new Dimension(400, 60));
        this.setBackground(lightColor);

        addTask = new JButton("Add Task"); 
        addTask.setBorder(emptyBorder); 
        addTask.setFont(new Font("Sans-serif", Font.BOLD, 20));
        addTask.setVerticalAlignment(JButton.BOTTOM); 
        addTask.setBackground(lightblue); 
        this.add(addTask); // add to footer

        this.add(Box.createHorizontalStrut(20)); 

        clear = new JButton("Clear finished Task"); 
        clear.setFont(new Font("Sans-serif", Font.BOLD, 20)); 
        clear.setBorder(emptyBorder); 
        clear.setBackground(lightblue); 
        this.add(clear); 
        
        this.add(Box.createHorizontalStrut(20));
        
        remindTask = new JButton("Set Task Reminder"); // Initialize the task reminder button
        remindTask.setFont(new Font("Sans-serif", Font.BOLD, 20));
        remindTask.setBorder(emptyBorder);
        remindTask.setBackground(lightblue);
        this.add(remindTask); // Add the task reminder button to the footer
        this.add(Box.createHorizontalStrut(20));
        
        logout = new JButton("Logout");
        logout.setFont(new Font("Sans-serif", Font.BOLD, 20));
        logout.setBorder(BorderFactory.createEmptyBorder());
        logout.setBackground(lightblue);
        this.add(logout);
        
        remindTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the task reminder functionality here
                // Schedule reminders for each task in the list
                Component[] listItems = list.getComponents();

                for (Component item : listItems) {
                    if (item instanceof Task) {
                        Task task = (Task) item;
                        String taskName = task.taskName.getText();

                        // Get the task reminder time (for this example, 5 seconds from the current time)
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.SECOND, 5); // Adjust the reminder time here

                        // Schedule the reminder task
                        Timer timer = new Timer();
                        timer.schedule(new ReminderTask(taskName), calendar.getTime());
                    }
                }
            }
        });
    }
    
    

    public JButton getNewTask() {
        return addTask;
    }

    public JButton getClear() {
        return clear;
    }

    public JButton getLogoutButton() {
        return logout;
    }
    
    public JButton getRemindTaskButton() {
        return remindTask; // Return the task reminder button
    }
    private class ReminderTask extends TimerTask {
        private String taskName;

        ReminderTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            // This method will be executed when the reminder time is reached
            // For this example, let's display a reminder message using a message dialog
            javax.swing.JOptionPane.showMessageDialog(
                    Footer.this, "Reminder for task: " + taskName);
        }
    }
}

class TitleBar extends JPanel {

    Color lightColor = new Color(255,255,204);

    TitleBar() {
        this.setPreferredSize(new Dimension(400, 80));
        this.setBackground(lightColor); 
        JLabel titleText = new JLabel("Daily Task Scheduler"); 
        titleText.setPreferredSize(new Dimension(400, 100)); 
        titleText.setFont(new Font("Sans-serif", Font.BOLD, 24)); 
        titleText.setHorizontalAlignment(JLabel.CENTER); 
        this.add(titleText); 
    }
}

class AppFrame extends JFrame {

    private TitleBar title;
    private Footer footer;
    private List list;

    private JButton newTask;
    private JButton clear;

    AppFrame() {
        this.setSize(400, 600); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setVisible(true); 

        title = new TitleBar();
        list = new List();
        footer = new Footer(list);
        

        this.add(title, BorderLayout.NORTH); 
        this.add(footer, BorderLayout.SOUTH); 
        this.add(list, BorderLayout.CENTER); 

        newTask = footer.getNewTask();
        clear = footer.getClear();
         JButton logout = footer.getLogoutButton();

        
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new Login(); 
            }

            
        });
        

        addListeners();
    }
    
    

    public void addListeners() {
        
     
        newTask.addMouseListener(new MouseAdapter() {
            @override
            public void mousePressed(MouseEvent e) {
                Task task = new Task();
                list.add(task); 
                list.updateNumbers(); 

                task.getDone().addMouseListener(new MouseAdapter() {
                    @override
                    public void mousePressed(MouseEvent e) {

                        task.changeState(); 
                        list.updateNumbers(); 
                        revalidate(); 

                    }
                });
            }

        });

        clear.addMouseListener(new MouseAdapter() {
            @override
            public void mousePressed(MouseEvent e) {
                list.removeCompletedTasks(); 
                repaint(); 
            }
        });
        
        
    }

}

public class Task_Scheduler {

    public static void main(String args[]) {
        AppFrame frame = new AppFrame(); 
    }
}

@interface override {

}

