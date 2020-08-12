package io.github.danifascio.gui;

import com.jfoenix.svg.SVGGlyph;
import io.github.danifascio.BundleManager;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Nullable;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class GlyphFactory {

	private static ResourceBundle paths;

	private GlyphFactory() {

	}

	public static void init() {
		if(paths == null)
			paths = BundleManager.load("glyphs", true);

	}

	public static @Nullable Node create(String glyph, Paint color, int size) {
		String path;

		try {
			path = paths.getString(glyph);
		} catch(MissingResourceException e) {
			return null;
		}

		Rectangle rectangle = new Rectangle(size, size);
		SVGGlyph svgGlyph = new SVGGlyph(path, color);
		StackPane stackPane = new StackPane(rectangle, svgGlyph);

		rectangle.setVisible(false);
		stackPane.setMaxSize(size, size);
		svgGlyph.setSize(size);

		return stackPane;
	}

}
