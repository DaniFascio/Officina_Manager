package io.github.danifascio.gui;

import com.jfoenix.svg.SVGGlyph;
import io.github.danifascio.BundleManager;
import io.github.danifascio.Gui;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Nullable;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class GlyphFactory {

	private GlyphFactory() {

	}

	public static @Nullable Node create(String glyph, Paint color, int size) {
		String path = Gui.iconPaths().getProperty(glyph);
		if(path == null)
			return null;

		Rectangle rectangle = new Rectangle(size, size);
		SVGGlyph svgGlyph = new SVGGlyph(path, color);
		StackPane stackPane = new StackPane(rectangle, svgGlyph);

		rectangle.setVisible(false);
		stackPane.setMaxSize(size, size);
		svgGlyph.setSize(size);

		return stackPane;
	}

}
