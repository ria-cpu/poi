
/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache POI" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache POI", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;

/**
 * Title:        Window Two Record<P>
 * Description:  sheet window settings<P>
 * REFERENCE:  PG 422 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @version 2.0-pre
 */

public class WindowTwoRecord
    extends Record
{
    public final static short sid = 0x23e;
    private short             field_1_options;

    // bitfields
    private BitField          displayFormulas         = new BitField(0x01);
    private BitField          displayGridlines        = new BitField(0x02);
    private BitField          displayRowColHeadings   = new BitField(0x04);
    private BitField          freezePanes             = new BitField(0x08);
    private BitField          displayZeros            = new BitField(0x10);
    private BitField          defaultHeader           =
        new BitField(0x20);   // if false use color in field 4

    // if true use default foreground
    // for headers
    private BitField          arabic                  =
        new BitField(0x40);   // for our desert dwelling friends
    private BitField          displayGuts             = new BitField(0x80);
    private BitField          freezePanesNoSplit      = new BitField(0x100);
    private BitField          selected                = new BitField(0x200);
    private BitField          paged                   = new BitField(0x400);
    private BitField          savedInPageBreakPreview = new BitField(0x800);

    // 4-7 reserved
    // end bitfields
    private short             field_2_top_row;
    private short             field_3_left_col;
    private int               field_4_header_color;
    private short             field_5_page_break_zoom;
    private short             field_6_normal_zoom;
    private int               field_7_reserved;

    public WindowTwoRecord()
    {
    }

    /**
     * Constructs a WindowTwo record and sets its fields appropriately.
     *
     * @param id     id must be 0x23e or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     */

    public WindowTwoRecord(short id, short size, byte [] data)
    {
        super(id, size, data);
    }

    /**
     * Constructs a WindowTwo record and sets its fields appropriately.
     *
     * @param id     id must be 0x23e or an exception will be throw upon validation
     * @param size  the size of the data area of the record
     * @param data  data of the record (should not contain sid/len)
     * @param offset of the record's data
     */

    public WindowTwoRecord(short id, short size, byte [] data, int offset)
    {
        super(id, size, data, offset);
    }

    protected void validateSid(short id)
    {
        if (id != sid)
        {
            throw new RecordFormatException("NOT A valid WindowTwo RECORD");
        }
    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_options      = LittleEndian.getShort(data, 0 + offset);
        field_2_top_row      = LittleEndian.getShort(data, 2 + offset);
        field_3_left_col     = LittleEndian.getShort(data, 4 + offset);
        field_4_header_color = LittleEndian.getInt(data, 6 + offset);
        if (size > 10)
        {
            field_5_page_break_zoom = LittleEndian.getShort(data,
                    10 + offset);
            field_6_normal_zoom     = LittleEndian.getShort(data,
                    12 + offset);
        }
        if (size > 14)
        {   // there is a special case of this record that has only 14 bytes...undocumented!
            field_7_reserved = LittleEndian.getInt(data, 14 + offset);
        }
    }

    /**
     * set the options bitmask or just use the bit setters.
     * @param options
     */

    public void setOptions(short options)
    {
        field_1_options = options;
    }

    // option bitfields

    /**
     * set whether the window should display formulas
     * @param formulas or not
     */

    public void setDisplayFormulas(boolean formulas)
    {
        displayFormulas.setBoolean(field_1_options, formulas);
    }

    /**
     * set whether the window should display gridlines
     * @param gridlines or not
     */

    public void setDisplayGridlines(boolean gridlines)
    {
        displayGridlines.setBoolean(field_1_options, gridlines);
    }

    /**
     * set whether the window should display row and column headings
     * @param headings or not
     */

    public void setDisplayRowColHeadings(boolean headings)
    {
        displayRowColHeadings.setBoolean(field_1_options, headings);
    }

    /**
     * set whether the window should freeze panes
     * @param freeze panes or not
     */

    public void setFreezePanes(boolean freezepanes)
    {
        freezePanes.setBoolean(field_1_options, freezepanes);
    }

    /**
     * set whether the window should display zero values
     * @param zeros or not
     */

    public void setDisplayZeros(boolean zeros)
    {
        displayZeros.setBoolean(field_1_options, zeros);
    }

    /**
     * set whether the window should display a default header
     * @param header or not
     */

    public void setDefaultHeader(boolean header)
    {
        defaultHeader.setBoolean(field_1_options, header);
    }

    /**
     * is this arabic?
     * @param arabic or not
     */

    public void setArabic(boolean isarabic)
    {
        arabic.setBoolean(field_1_options, isarabic);
    }

    /**
     * set whether the outline symbols are displaed
     * @param symbols or not
     */

    public void setDisplayGuts(boolean guts)
    {
        displayGuts.setBoolean(field_1_options, guts);
    }

    /**
     * freeze unsplit panes or not
     * @param freeze or not
     */

    public void setFreezePanesNoSplit(boolean freeze)
    {
        freezePanesNoSplit.setBoolean(field_1_options, freeze);
    }

    /**
     * sheet tab is selected
     * @param selected or not
     */

    public void setSelected(boolean sel)
    {
        selected.setBoolean(field_1_options, sel);
    }

    /**
     * is the sheet currently displayed in the window
     * @param displayed or not
     */

    public void setPaged(boolean p)
    {
        paged.setBoolean(field_1_options, p);
    }

    /**
     * was the sheet saved in page break view
     * @param pagebreaksaved or not
     */

    public void setSavedInPageBreakPreview(boolean p)
    {
        savedInPageBreakPreview.setBoolean(field_1_options, p);
    }

    // end of bitfields.

    /**
     * set the top row visible in the window
     * @param toprow
     */

    public void setTopRow(short topRow)
    {
        field_2_top_row = topRow;
    }

    /**
     * set the leftmost column displayed in the window
     * @param leftmost
     */

    public void setLeftCol(short leftCol)
    {
        field_3_left_col = leftCol;
    }

    /**
     * set the palette index for the header color
     * @param color
     */

    public void setHeaderColor(int color)
    {
        field_4_header_color = color;
    }

    /**
     * zoom magification in page break view
     * @param zoom
     */

    public void setPageBreakZoom(short zoom)
    {
        field_5_page_break_zoom = zoom;
    }

    /**
     * set the zoom magnification in normal view
     * @param zoom
     */

    public void setNormalZoom(short zoom)
    {
        field_6_normal_zoom = zoom;
    }

    /**
     * set the reserved (don't do this) value
     */

    public void setReserved(int reserved)
    {
        field_7_reserved = reserved;
    }

    /**
     * get the options bitmask or just use the bit setters.
     * @return options
     */

    public short getOptions()
    {
        return field_1_options;
    }

    // option bitfields

    /**
     * get whether the window should display formulas
     * @return formulas or not
     */

    public boolean getDisplayFormulas()
    {
        return displayFormulas.isSet(field_1_options);
    }

    /**
     * get whether the window should display gridlines
     * @return gridlines or not
     */

    public boolean getDisplayGridlines()
    {
        return displayGridlines.isSet(field_1_options);
    }

    /**
     * get whether the window should display row and column headings
     * @return headings or not
     */

    public boolean getDisplayRowColHeadings()
    {
        return displayRowColHeadings.isSet(field_1_options);
    }

    /**
     * get whether the window should freeze panes
     * @return freeze panes or not
     */

    public boolean getFreezePanes()
    {
        return freezePanes.isSet(field_1_options);
    }

    /**
     * get whether the window should display zero values
     * @return zeros or not
     */

    public boolean getDisplayZeros()
    {
        return displayZeros.isSet(field_1_options);
    }

    /**
     * get whether the window should display a default header
     * @return header or not
     */

    public boolean getDefaultHeader()
    {
        return defaultHeader.isSet(field_1_options);
    }

    /**
     * is this arabic?
     * @return arabic or not
     */

    public boolean getArabic()
    {
        return arabic.isSet(field_1_options);
    }

    /**
     * get whether the outline symbols are displaed
     * @return symbols or not
     */

    public boolean getDisplayGuts()
    {
        return displayGuts.isSet(field_1_options);
    }

    /**
     * freeze unsplit panes or not
     * @return freeze or not
     */

    public boolean getFreezePanesNoSplit()
    {
        return freezePanesNoSplit.isSet(field_1_options);
    }

    /**
     * sheet tab is selected
     * @return selected or not
     */

    public boolean getSelected()
    {
        return selected.isSet(field_1_options);
    }

    /**
     * is the sheet currently displayed in the window
     * @return displayed or not
     */

    public boolean getPaged()
    {
        return paged.isSet(field_1_options);
    }

    /**
     * was the sheet saved in page break view
     * @return pagebreaksaved or not
     */

    public boolean getSavedInPageBreakPreview()
    {
        return savedInPageBreakPreview.isSet(field_1_options);
    }

    // end of bitfields.

    /**
     * get the top row visible in the window
     * @return toprow
     */

    public short getTopRow()
    {
        return field_2_top_row;
    }

    /**
     * get the leftmost column displayed in the window
     * @return leftmost
     */

    public short getLeftCol()
    {
        return field_3_left_col;
    }

    /**
     * get the palette index for the header color
     * @return color
     */

    public int getHeaderColor()
    {
        return field_4_header_color;
    }

    /**
     * zoom magification in page break view
     * @return zoom
     */

    public short getPageBreakZoom()
    {
        return field_5_page_break_zoom;
    }

    /**
     * get the zoom magnification in normal view
     * @return zoom
     */

    public short getNormalZoom()
    {
        return field_6_normal_zoom;
    }

    /**
     * get the reserved bits - why would you do this?
     * @return reserved stuff -probably garbage
     */

    public int getReserved()
    {
        return field_7_reserved;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[WINDOW2]\n");
        buffer.append("    .options        = ")
            .append(Integer.toHexString(getOptions())).append("\n");
        buffer.append("       .dispformulas= ").append(getDisplayFormulas())
            .append("\n");
        buffer.append("       .dispgridlins= ").append(getDisplayGridlines())
            .append("\n");
        buffer.append("       .disprcheadin= ")
            .append(getDisplayRowColHeadings()).append("\n");
        buffer.append("       .freezepanes = ").append(getFreezePanes())
            .append("\n");
        buffer.append("       .displayzeros= ").append(getDisplayZeros())
            .append("\n");
        buffer.append("       .defaultheadr= ").append(getDefaultHeader())
            .append("\n");
        buffer.append("       .arabic      = ").append(getArabic())
            .append("\n");
        buffer.append("       .displayguts = ").append(getDisplayGuts())
            .append("\n");
        buffer.append("       .frzpnsnosplt= ")
            .append(getFreezePanesNoSplit()).append("\n");
        buffer.append("       .selected    = ").append(getSelected())
            .append("\n");
        buffer.append("       .paged       = ").append(getPaged())
            .append("\n");
        buffer.append("       .svdinpgbrkpv= ")
            .append(getSavedInPageBreakPreview()).append("\n");
        buffer.append("    .toprow         = ")
            .append(Integer.toHexString(getTopRow())).append("\n");
        buffer.append("    .leftcol        = ")
            .append(Integer.toHexString(getLeftCol())).append("\n");
        buffer.append("    .headercolor    = ")
            .append(Integer.toHexString(getHeaderColor())).append("\n");
        buffer.append("    .pagebreakzoom  = ")
            .append(Integer.toHexString(getPageBreakZoom())).append("\n");
        buffer.append("    .normalzoom     = ")
            .append(Integer.toHexString(getNormalZoom())).append("\n");
        buffer.append("    .reserved       = ")
            .append(Integer.toHexString(getReserved())).append("\n");
        buffer.append("[/WINDOW2]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte [] data)
    {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ( short ) 18);
        LittleEndian.putShort(data, 4 + offset, getOptions());
        LittleEndian.putShort(data, 6 + offset, getTopRow());
        LittleEndian.putShort(data, 8 + offset, getLeftCol());
        LittleEndian.putInt(data, 10 + offset, getHeaderColor());
        LittleEndian.putShort(data, 14 + offset, getPageBreakZoom());
        LittleEndian.putShort(data, 16 + offset, getNormalZoom());
        LittleEndian.putInt(data, 18 + offset, getReserved());
        return getRecordSize();
    }

    public int getRecordSize()
    {
        return 22;
    }

    public short getSid()
    {
        return this.sid;
    }
}
