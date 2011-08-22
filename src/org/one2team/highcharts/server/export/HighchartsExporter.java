package org.one2team.highcharts.server.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.one2team.highcharts.server.export.util.SVGRendererInternal;

public class HighchartsExporter<I> {

	public HighchartsExporter(ExportType type, SVGRendererInternal<I> internalRenderer) {
		this.type = type;
		this.renderer = 
			new SVGStreamRenderer<I> (new SVGRenderer<I> (internalRenderer),
				                     type.getTranscoder ());
	}

	public void export (I chartOptions,
			                I globalOptions,
			                File file) {
		
		OutputStream fos = null;
		try {
			fos = render (chartOptions, globalOptions, file);

		} catch (Exception e) {
			e.printStackTrace ();
			throw (new RuntimeException (e));
		} finally {
			if (fos != null)
				IOUtils.closeQuietly (fos);
		}
	}

	private OutputStream render (I chartOptions,
			                         I globalOptions,
                               File file) throws FileNotFoundException {
		FileOutputStream fos;
		renderer.setChartOptions (chartOptions)
				    .setGlobalOptions (globalOptions)
				    .setOutputStream (fos = new FileOutputStream (file))
				    .render ();
		return fos;
	}

	public SVGStreamRenderer<I> getRenderer () {
		return renderer;
	}

	public ExportType getType () {
		return type;
	}

	private final SVGStreamRenderer<I> renderer;

	private final ExportType type;
}
