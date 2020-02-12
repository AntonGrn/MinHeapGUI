import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TreeController {

    @FXML
    private VBox root;
    @FXML
    private Pane nodePane, buttonPane;
    @FXML
    private Button btnInOrder, btnPreOrder, btnPostOrder, btnLevelOrder, btnN, btnNLogN;
    @FXML
    private Circle c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15;
    @FXML
    private Label t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, infoLabel;
    @FXML
    private TextField f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15; // Input text fields
    @FXML
    private TextField d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15, runtimeField; //Result text fields

    private int[] origItems = {10, 12, 1, 14, 6, 5, 8, 15, 3, 9, 7, 4, 11, 13, 2};
    private int[] items;
    private int[] heap;

    private Circle[] circle;
    private Label[] label;
    private TextField[] textField;
    private TextField[] resultField;


    public void initialize() {

        circle = new Circle[]{c1, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15};
        label = new Label[]{t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15};
        textField = new TextField[]{f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15};
        resultField = new TextField[]{d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15};
        items = new int[15];
        heap = new int[16];

        //Draw lines
        for (int i = 1; i < 8; i++) {
            Line line1 = new Line(circle[i].getLayoutX(), circle[i].getLayoutY(), circle[i * 2].getLayoutX(), circle[i * 2].getLayoutY());
            Line line2 = new Line(circle[i].getLayoutX(), circle[i].getLayoutY(), circle[i * 2 + 1].getLayoutX(), circle[i * 2 + 1].getLayoutY());
            nodePane.getChildren().addAll(line1, line2);
            line1.toBack();
            line2.toBack();
        }

        btnInOrder.setUserData("inOrder");
        btnPreOrder.setUserData("preOrder");
        btnPostOrder.setUserData("postOrder");
        btnLevelOrder.setUserData("levelOrder");
        btnN.setUserData("N");
        btnNLogN.setUserData("NLogN");

        reset();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                root.requestFocus(); // Remove default cursor from TextField
            }
        });
    }

    public void reset() {
        //Set initial tree
        for (int i = 0; i < 15; i++) {
            items[i] = origItems[i];
            textField[i].setText(String.valueOf(items[i]));
            label[i].setText(textField[i].getText());
        }
        for (int i = 0; i < 16; i++) {
            heap[i] = -1;
        }
        infoLabel.setText("Built: None");
        displayResult();
    }

    public void displayResult() {
        for (int i = 0; i < 15; i++) {
            if(heap[i+1] == -1) {
                label[i].setText("");
                circle[i+1].setStyle("-fx-fill: #36373B;"); //grey'
            } else {
                label[i].setText(String.valueOf(heap[i + 1]));
                circle[i+1].setStyle("-fx-fill: #274AFA;"); //blue
            }
        }
        btnLevelOrder.fire();
    }

    public void buildHeap(Event event) {
        String algorithm = ((Button) event.getSource()).getUserData().toString();
        try {
            gatherInput();
            BinaryHeap bin = new BinaryHeap(items);
            heap = algorithm.equals("NLogN") ? bin.buildHeap_NlogN() : bin.buildHeap_N();
            infoLabel.setText("Built: " + (algorithm.equals("NLogN") ? "O(NLogN)" : "O(N)"));
            displayResult();
            String runtime = String.format("%.4f", bin.avgRuntime);
            runtimeField.setText(runtime);
        } catch (NumberFormatException e) {
            infoLabel.setText("Invalid input");
        }
    }

    public void gatherInput() throws NumberFormatException {
        for (int i = 0; i < 15; i++) {
            items[i] = Integer.parseInt(textField[i].getText().trim());
        }
    }

    @FXML
    public void traverse(Event event) {
        Traversal traversal = new Traversal();
        int[] tree = null;

        switch(((Button) event.getSource()).getUserData().toString()) {
            case "inOrder":
                tree = traversal.inOrder(heap);
                break;
            case "preOrder":
                tree = traversal.preOrder(heap);
                break;
            case "postOrder":
                tree = traversal.postOrder(heap);
                break;
            case "levelOrder":
                tree = heap;
                break;
        }
        updateResultField(tree);

        //Set all buttons to default css.
        for (Node node : buttonPane.getChildren()) {
            if (node instanceof Button) {
                node.setStyle("");
            }
        }
        ((Button) event.getSource()).setStyle("-fx-background-color:gray");

    }
    public void updateResultField(int[] tree) {
        for (int i = 0; i < 16; i++) {
            resultField[i].setText(String.valueOf(tree[i]));
            if(tree[i] == -1) {
                resultField[i].setStyle("-fx-text-fill: #9e1616;");
            } else {
                resultField[i].setStyle("");
            }
        }
    }

}
