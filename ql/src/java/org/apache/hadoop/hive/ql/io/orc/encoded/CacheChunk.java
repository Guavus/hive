package org.apache.hadoop.hive.ql.io.orc.encoded;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.hadoop.hive.common.io.DataCache;
import org.apache.hadoop.hive.common.io.DiskRange;
import org.apache.hadoop.hive.common.io.DiskRangeList;
import org.apache.hadoop.hive.common.io.encoded.MemoryBuffer;

import com.google.common.annotations.VisibleForTesting;

/**
 * DiskRange containing encoded, uncompressed data from cache.
 * It should be hidden inside EncodedReaderImpl, but we need to expose it for tests.
 */
@VisibleForTesting
public class CacheChunk extends DiskRangeList {
  protected MemoryBuffer buffer;

  public CacheChunk() {
    super(-1, -1);
  }

  public void init(MemoryBuffer buffer, long offset, long end) {
    this.buffer = buffer;
    this.offset = offset;
    this.end = end;
  }

  @Override
  public boolean hasData() {
    return buffer != null;
  }

  @Override
  public ByteBuffer getData() {
    // Callers duplicate the buffer, they have to for BufferChunk; so we don't have to.
    return buffer.getByteBufferRaw();
  }

  @Override
  public String toString() {
    return "start: " + offset + " end: " + end + " cache buffer: " + getBuffer();
  }

  @Override
  public DiskRange sliceAndShift(long offset, long end, long shiftBy) {
    throw new UnsupportedOperationException("Cache chunk cannot be sliced - attempted ["
        + this.offset + ", " + this.end + ") to [" + offset + ", " + end + ") ");
  }

  public MemoryBuffer getBuffer() {
    return buffer;
  }

  public void setBuffer(MemoryBuffer buffer) {
    this.buffer = buffer;
  }

  public void handleCacheCollision(DataCache cache,
      MemoryBuffer replacementBuffer, List<MemoryBuffer> cacheBuffers) {
    throw new UnsupportedOperationException();
  }

  public void reset() {
    init(null, -1, -1);
  }
}