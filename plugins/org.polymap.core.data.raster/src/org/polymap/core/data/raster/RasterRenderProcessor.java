/* 
 * polymap.org
 * Copyright (C) 2010-2015, Polymap GmbH. All rights reserved.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.polymap.core.data.raster;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.ContrastMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.polymap.core.data.image.GetLayerTypesRequest;
import org.polymap.core.data.image.GetLegendGraphicRequest;
import org.polymap.core.data.image.GetMapRequest;
import org.polymap.core.data.image.ImageProducer;
import org.polymap.core.data.pipeline.DataSourceDescription;
import org.polymap.core.data.pipeline.PipelineExecutor.ProcessorContext;
import org.polymap.core.data.pipeline.PipelineProcessorSite;
import org.polymap.core.data.pipeline.TerminalPipelineProcessor;

/**
 * This processor renders raster data of {@link AbstractGridCoverage2DReader} using
 * the geotools {@link StreamingRenderer}.
 * 
 * @deprecated Not yet completely ported to Polymap4.
 * @author <a href="http://www.polymap.de">Falko Bräutigam</a>
 */
public class RasterRenderProcessor
        implements ImageProducer, TerminalPipelineProcessor {

    private static final Log log = LogFactory.getLog( RasterRenderProcessor.class );

    private StyleFactory            sf = CommonFactoryFinder.getStyleFactory( null );
    
    private FilterFactory2          ff = CommonFactoryFinder.getFilterFactory2( null );
    
    protected MapContent            mapContent;
    
    /** The styles used in the current {@link #mapContent}, used to check if new context is needed. */
//    protected Map<ILayer,Style>     styles = new HashMap();
    
    
    @Override
    public void init( PipelineProcessorSite site ) throws Exception {
    }


    @Override
    public boolean isCompatible( DataSourceDescription dsd ) {
        return dsd.service.get() instanceof AbstractGridCoverage2DReader;
    }


    @Override
    public void getLegendGraphicRequest( GetLegendGraphicRequest request, ProcessorContext context ) throws Exception {
        // XXX Auto-generated method stub
        throw new RuntimeException( "not yet implemented." );
    }


    @Override
    public void getLayerTypesRequest( GetLayerTypesRequest request, ProcessorContext context ) throws Exception {
        // XXX Auto-generated method stub
        throw new RuntimeException( "not yet implemented." );
    }


    @Override
    public void getMapRequest( GetMapRequest request, ProcessorContext context ) throws Exception {
        throw new RuntimeException( "Not yet completely ported to Polymap4." );
        // mapContext
//        synchronized (this) {
//            // check style objects
//            boolean needsNewContext = false;
//
//            // create mapContext
//            if (mapContent == null || needsNewContext) {
//                // sort z-priority
//                TreeMap<String,ILayer> sortedLayers = new TreeMap();
//                for (ILayer layer : layers) {
//                    String uniqueOrderKey = String.valueOf( layer.getOrderKey() ) + layer.id();
//                    sortedLayers.put( uniqueOrderKey, layer );
//                }
//                // add to mapContext
//                mapContent = new DefaultMapContext( bbox.getCoordinateReferenceSystem() );
//                for (ILayer layer : sortedLayers.values()) {
//                    try {
//                        IGeoResource res = layer.getGeoResource();
//                        if (res == null) {
//                            throw new IllegalStateException( "Unable to find geo resource of layer: " + layer );
//                        }
//                        AbstractRasterService service = (AbstractRasterService)res.service( null );
//                        log.debug( "    service: " + service );
//                        
//                        log.debug( "    CRS: " + layer.getCRS() );
//                        AbstractGridCoverage2DReader reader = service.getReader( layer.getCRS(), null );
//                        
//                        Style style = createRGBStyle( reader );
//                        if (style == null) {
//                            log.warn( "Error creating RGB style, trying greyscale..." );
//                            style = createGreyscaleStyle( 1 );
//                        }
//                        mapContent.addLayer( reader, style );
//                        styles.put( layer, style );
//                    }
//                    catch (IOException e) {
//                        log.warn( e );
//                        // FIXME set layer status and statusMessage
//                    }
//                }
//            }
//            else {
//            }
//        }
//        
//        // render
//        BufferedImage result = new BufferedImage( request.getWidth(), request.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
//        final Graphics2D g = result.createGraphics();
//        try {
//            StreamingRenderer renderer = new StreamingRenderer();
//
//            // error handler
//            renderer.addRenderListener( new RenderListener() {
//                public void featureRenderer( SimpleFeature feature ) {
//                }
//                public void errorOccurred( Exception e ) {
//                    log.error( "Renderer error: ", e );
//                    drawErrorMsg( g, "Fehler bei der Darstellung.", e );
//                }
//            });
//
//            // rendering hints
//            RenderingHints hints = new RenderingHints(
//                    RenderingHints.KEY_RENDERING,
//                    RenderingHints.VALUE_RENDER_QUALITY );
//            hints.add( new RenderingHints(
//                    RenderingHints.KEY_ANTIALIASING,
//                    RenderingHints.VALUE_ANTIALIAS_ON ) );
//            hints.add( new RenderingHints(
//                    RenderingHints.KEY_TEXT_ANTIALIASING,
//                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON ) );
//
//            renderer.setJava2DHints( hints );
////            g.setRenderingHints( hints );
//
//            // render params
//            Map rendererParams = new HashMap();
//            rendererParams.put( "optimizedDataLoadingEnabled", Boolean.TRUE );
//            renderer.setRendererHints( rendererParams );
//            
//            renderer.setMapContent( mapContent );
//            Rectangle paintArea = new Rectangle( request.getWidth(), request.getHeight() );
//            renderer.paint( g, paintArea, request.getBoundingBox() );
//        }
//        catch (Throwable e) {
//            log.error( "Renderer error: ", e );
//            drawErrorMsg( g, null, e );
//        }
//        finally {
//            if (g != null) { g.dispose(); }
//        }
//        context.sendResponse( new ImageResponse( result ) );
    }

    
    protected void drawErrorMsg( Graphics2D g, String msg, Throwable e ) {
        g.setColor( Color.RED );
        g.setStroke( new BasicStroke( 1 ) );
        g.getFont().deriveFont( Font.BOLD, 12 );
        if (msg != null) {
            g.drawString( msg, 10, 10 );
        }
        if (e != null) {
            g.drawString( e.toString(), 10, 30 );
        }
    }


    /**
     * This method examines the names of the sample dimensions in the provided
     * coverage looking for "red...", "green..." and "blue..." (case insensitive
     * match). If these names are not found it uses bands 1, 2, and 3 for the
     * red, green and blue channels. It then sets up a raster symbolizer and
     * returns this wrapped in a Style.
     * 
     * @return A new Style object containing a raster symbolizer set up for RGB
     *         image.
     */
    protected Style createRGBStyle( AbstractGridCoverage2DReader reader ) {
        GridCoverage2D cov = null;
        try {
            cov = reader.read( null );
        }
        catch (IOException giveUp) {
            throw new RuntimeException( giveUp );
        }
        
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            return null;
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension( i );
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = { -1, -1, -1 };
        // We examine the band names looking for "red...", "green...",
        // "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches( "red.*" )) {
                    channelNum[RED] = i + 1;
                }
                else if (name.matches( "green.*" )) {
                    channelNum[GREEN] = i + 1;
                }
                else if (name.matches( "blue.*" )) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement( ff.literal( 1.0 ),
                ContrastMethod.NORMALIZE );
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType( String.valueOf( channelNum[i] ), ce );
        }
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection( sct[RED], sct[GREEN], sct[BLUE] );
        sym.setChannelSelection( sel );

        return SLD.wrapSymbolizers( sym );
    }

    
//    /**
//     * Create a Style to display a selected band of the GeoTIFF image as a
//     * greyscale layer
//     * 
//     * @return a new Style instance to render the image in greyscale
//     */
//    protected Style createGreyscaleStyle( AbstractGridCoverage2DReader reader ) {
//        GridCoverage2D cov = null;
//        try {
//            cov = reader.read(null);
//        } catch (IOException giveUp) {
//            throw new RuntimeException(giveUp);
//        }
//        int numBands = cov.getNumSampleDimensions();
//        Integer[] bandNumbers = new Integer[numBands];
//        for (int i = 0; i < numBands; i++) { bandNumbers[i] = i+1; }
//        Object selection = JOptionPane.showInputDialog(
//                frame,
//                "Band to use for greyscale display",
//                "Select an image band",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                bandNumbers,
//                1);
//        if (selection != null) {
//            int band = ((Number)selection).intValue();
//            return createGreyscaleStyle(band);
//        }
//        return null;
//    }


    /**
     * Create a Style to display the specified band of the GeoTIFF image as a
     * greyscale layer.
     * <p>
     * This method is a helper for createGreyScale() and is also called directly
     * by the displayLayers() method when the application first starts.
     * 
     * @param band the image band to use for the greyscale display
     * 
     * @return a new Style instance to render the image in greyscale
     */
    private Style createGreyscaleStyle(int band) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(String.valueOf(band), ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

}
