import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class TodoListApp {
    private JFrame frame;
    private DefaultListModel<Task> todoListModel;
    private JList<Task> todoList;
    private JTextField taskTextField;
    private JComboBox<String> priorityComboBox;
    private JButton addButton;
    private JButton removeButton;
    private JCheckBox completedCheckBox;

    public TodoListApp() {
        frame = new JFrame("To-Do List App");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoList.setCellRenderer(new TaskRenderer());

        JScrollPane scrollPane = new JScrollPane(todoList);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel textAddPanel = new JPanel(new BorderLayout());

        taskTextField = new JTextField();
        textAddPanel.add(taskTextField, BorderLayout.CENTER);

        addButton = new JButton("Add");
        addButton.addActionListener(e -> addTask());
        textAddPanel.add(addButton, BorderLayout.EAST);

        String[] priorities = {"High","Medium","Low"};
        priorityComboBox = new JComboBox<>(priorities);
        inputPanel.add(priorityComboBox, BorderLayout.WEST);
        inputPanel.add(textAddPanel, BorderLayout.CENTER);

        removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> removeTask());
        inputPanel.add(removeButton, BorderLayout.EAST);

        completedCheckBox = new JCheckBox("Mark Completed");
        completedCheckBox.addActionListener(e -> markTaskCompleted());
        inputPanel.add(completedCheckBox, BorderLayout.NORTH);

        todoList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = todoList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < todoListModel.getSize() - 1) {
                        completedCheckBox.setSelected(false);
                    }
                }
            }
        });

        taskTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addTask() {
        String taskText = taskTextField.getText().trim();
        if (!taskText.isEmpty()) {
            String priority = (String) priorityComboBox.getSelectedItem();
            Task task = new Task(taskText, priority);
            todoListModel.addElement(task);
            taskTextField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Task cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeTask() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            todoListModel.removeElementAt(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(frame, "No task selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markTaskCompleted() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = todoListModel.getElementAt(selectedIndex);
            selectedTask.setCompleted(completedCheckBox.isSelected());
            todoList.repaint();
        } else {
            JOptionPane.showMessageDialog(frame, "No task selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class Task {
        private String text;
        private String priority;
        private boolean completed;

        public Task(String text, String priority) {
            this.text = text;
            this.priority = priority;
            this.completed = false;
        }

        public String getText() {
            return text;
        }

        public String getPriority() {
            return priority;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        @Override
        public String toString() {
            return text+" (" +priority+ ")";
        }
    }

    private class TaskRenderer extends JPanel implements ListCellRenderer<Task> {
        private JLabel label;
        private Task task;

        public TaskRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel();
            add(label, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index, boolean isSelected, boolean cellHasFocus) {
            this.task = task;
            label.setText(task.toString());

            if (isSelected) {
                if (completedCheckBox.isSelected() && task.isCompleted()) {
                    setBackground(Color.GREEN.brighter());
                } else {
                    setBackground(list.getSelectionBackground());
                }
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            if (task.isCompleted()) {
                label.setForeground(Color.GREEN.darker());
            } else {
                label.setForeground(Color.BLACK);
            }

            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoListApp());
    }
}
