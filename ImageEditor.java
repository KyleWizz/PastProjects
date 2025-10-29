package uk.ac.nulondon;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


/*APPLICATION SERVICE LAYER*/


/**
 * Image editor edits the image files
 */
public class ImageEditor {
    // current image being edited
    private Image image;
    //currently highlighted seam
    private List<Pixel> highlightedSeam = null;
    // deque stores command history
    private final Deque<Command> commandHistory = new ArrayDeque<>();

    // Command interface for encapsulating editing
    interface Command {
        void execute() throws IOException;
        void undo() throws IOException;
    }
    // Loads image from specified file path
    public void load(String filePath) throws IOException {
        File originalFile = new File(filePath);
        BufferedImage img = ImageIO.read(originalFile);
        image = new Image(img);
    }

    // Saves current image to specified file path
    public void save(String filePath) throws IOException {
        BufferedImage img = image.toBufferedImage();
        ImageIO.write(img, "png", new File(filePath));
    }

    /**
     * Highlight greenest pixel and execute the function using command panel
     * @throws IOException
     */
    public void highlightGreenest() throws IOException {
        Command command = new HighlightGreenestCommand(this);
        executeCommand(command);
        commandHistory.push(command);
    }

    /**
     * Removes the highlight method through command
     * @throws IOException
     */
    public void removeHighlighted() throws IOException {
        Command command = new RemoveHighlightedCommand(this);
        executeCommand(command);
        commandHistory.push(command);
    }

    /**
     * Undoes the image changes through a deque and command
     * @throws IOException
     */
    public void undo() throws IOException {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
    }

    /**
     * Highlights the lowest energy seam and gets value
     * @throws IOException
     */
    public void highlightLowestEnergySeam() throws IOException {
        //TODO: implement via Command pattern
        Command command = new HighlightLowestEnergyCommand(this);
        executeCommand(command);
        commandHistory.push(command);
    }

    /**
     * Command panel to highlight lowest energy command,
     * We edit the image and highlight seam after getting lowest energy then made buffered image
     */
    class HighlightLowestEnergyCommand implements Command {
        private final ImageEditor editor; // reference to ImageEditor instance
        private List<Pixel> lowestEnergySeam; // Stores the highlighted seam for undo

        // constructor for editor instance
        private HighlightLowestEnergyCommand(ImageEditor editor) {
            this.editor = editor;
        }

        @Override
        public void execute() throws IOException {
            lowestEnergySeam = editor.image.getLowestEnergySeam();
            editor.highlightedSeam = editor.image.highlightSeam(lowestEnergySeam, Color.RED);
            // Creates seprerate highlighted png
            editor.save("target/highlightedgreenest.png");
        }

        @Override
        public void undo() throws IOException {
            editor.image.removeSeam(lowestEnergySeam);
            editor.image.addSeam(lowestEnergySeam);
        }
    }
    // Executes a given command and adds it to history for undo
    private void executeCommand(Command command) throws IOException {
        command.execute();
        commandHistory.push(command);
    }
    //TODO: Implement Command class or interface and its subtypes

    /**
     * Puts colors in deque and gets our greenest command
     */
    class HighlightGreenestCommand implements Command {
        private final ImageEditor editor; // Reference to the editor instance
        private List<Pixel> greenestSeam; // Stores seam for undo
        // constructor for editor instance
        public HighlightGreenestCommand(ImageEditor editor) {
            this.editor = editor;
        }

        @Override
        public void execute() throws IOException {
            greenestSeam = editor.image.getGreenestSeam();
            editor.highlightedSeam = editor.image.highlightSeam(greenestSeam, Color.RED);
            editor.save("target/highlighted.png");
        }

        @Override
        public void undo() throws IOException {
            editor.image.removeSeam(greenestSeam);
            editor.image.addSeam(greenestSeam);
        }
    }

    /**
     * Remove the highlighted seams through remove seam
     */
    class RemoveHighlightedCommand implements Command {
        private final ImageEditor editor; // Reference to the editor instance
        private List<Pixel> originalSeam; // Stores seam for undo

        // constructor for editor instance
        public RemoveHighlightedCommand(ImageEditor editor) {
            this.editor = editor;
        }

        /***
         * we execute the undo method to get the greenest row and that makes it
         * so that it restores previous one
         * @throws IOException
         */
        @Override
        public void execute() throws IOException {
            originalSeam = editor.highlightedSeam;

            if (originalSeam != null) {
                editor.image.removeSeam(originalSeam);
                editor.highlightedSeam = null;
            } else {
                System.out.println("Nothing to remove");
            }
        }
        @Override
        public void undo() throws IOException {
            if (originalSeam != null) {
                editor.image.addSeam(originalSeam);
                editor.highlightedSeam = originalSeam;
            }
        }
    }
}