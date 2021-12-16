package uk.ac.shef.oak.com4510.model.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ImageDataDao_Impl implements ImageDataDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ImageData> __insertionAdapterOfImageData;

  private final EntityDeletionOrUpdateAdapter<ImageData> __deletionAdapterOfImageData;

  private final EntityDeletionOrUpdateAdapter<ImageData> __updateAdapterOfImageData;

  public ImageDataDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfImageData = new EntityInsertionAdapter<ImageData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `image` (`id`,`uri`,`title`,`description`,`thumbnailUri`,`position`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ImageData value) {
        stmt.bindLong(1, value.getId());
        if (value.getImageUri() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImageUri());
        }
        if (value.getImageTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getImageTitle());
        }
        if (value.getImageDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getImageDescription());
        }
        if (value.getThumbnailUri() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getThumbnailUri());
        }
        if (value.getPosition() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPosition());
        }
      }
    };
    this.__deletionAdapterOfImageData = new EntityDeletionOrUpdateAdapter<ImageData>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `image` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ImageData value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfImageData = new EntityDeletionOrUpdateAdapter<ImageData>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `image` SET `id` = ?,`uri` = ?,`title` = ?,`description` = ?,`thumbnailUri` = ?,`position` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ImageData value) {
        stmt.bindLong(1, value.getId());
        if (value.getImageUri() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImageUri());
        }
        if (value.getImageTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getImageTitle());
        }
        if (value.getImageDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getImageDescription());
        }
        if (value.getThumbnailUri() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getThumbnailUri());
        }
        if (value.getPosition() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPosition());
        }
        stmt.bindLong(7, value.getId());
      }
    };
  }

  @Override
  public Object insert(final ImageData singleImageData,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfImageData.insertAndReturnId(singleImageData);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final ImageData imageData, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfImageData.handle(imageData);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final ImageData imageData, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfImageData.handle(imageData);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getItems(final Continuation<? super List<ImageData>> continuation) {
    final String _sql = "SELECT * from image ORDER by id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ImageData>>() {
      @Override
      public List<ImageData> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfImageTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfImageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfThumbnailUri = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUri");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final List<ImageData> _result = new ArrayList<ImageData>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final ImageData _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            final String _tmpImageTitle;
            if (_cursor.isNull(_cursorIndexOfImageTitle)) {
              _tmpImageTitle = null;
            } else {
              _tmpImageTitle = _cursor.getString(_cursorIndexOfImageTitle);
            }
            final String _tmpImageDescription;
            if (_cursor.isNull(_cursorIndexOfImageDescription)) {
              _tmpImageDescription = null;
            } else {
              _tmpImageDescription = _cursor.getString(_cursorIndexOfImageDescription);
            }
            final String _tmpThumbnailUri;
            if (_cursor.isNull(_cursorIndexOfThumbnailUri)) {
              _tmpThumbnailUri = null;
            } else {
              _tmpThumbnailUri = _cursor.getString(_cursorIndexOfThumbnailUri);
            }
            final String _tmpPosition;
            if (_cursor.isNull(_cursorIndexOfPosition)) {
              _tmpPosition = null;
            } else {
              _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            }
            _item = new ImageData(_tmpId,_tmpImageUri,_tmpImageTitle,_tmpImageDescription,_tmpThumbnailUri,_tmpPosition);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object search(final String query,
      final Continuation<? super List<ImageData>> continuation) {
    final String _sql = "\n"
            + "    SELECT *\n"
            + "    FROM image\n"
            + "    JOIN image_fts ON image.title = image_fts.title\n"
            + "    WHERE image_fts MATCH ?\n"
            + "  ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ImageData>>() {
      @Override
      public List<ImageData> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfImageTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfImageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfThumbnailUri = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUri");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfImageTitle_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfImageDescription_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<ImageData> _result = new ArrayList<ImageData>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final ImageData _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            final String _tmpImageTitle;
            if (_cursor.isNull(_cursorIndexOfImageTitle)) {
              _tmpImageTitle = null;
            } else {
              _tmpImageTitle = _cursor.getString(_cursorIndexOfImageTitle);
            }
            final String _tmpImageDescription;
            if (_cursor.isNull(_cursorIndexOfImageDescription)) {
              _tmpImageDescription = null;
            } else {
              _tmpImageDescription = _cursor.getString(_cursorIndexOfImageDescription);
            }
            final String _tmpThumbnailUri;
            if (_cursor.isNull(_cursorIndexOfThumbnailUri)) {
              _tmpThumbnailUri = null;
            } else {
              _tmpThumbnailUri = _cursor.getString(_cursorIndexOfThumbnailUri);
            }
            final String _tmpPosition;
            if (_cursor.isNull(_cursorIndexOfPosition)) {
              _tmpPosition = null;
            } else {
              _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            }
            final String _tmpImageTitle_1;
            if (_cursor.isNull(_cursorIndexOfImageTitle_1)) {
              _tmpImageTitle_1 = null;
            } else {
              _tmpImageTitle_1 = _cursor.getString(_cursorIndexOfImageTitle_1);
            }
            final String _tmpImageDescription_1;
            if (_cursor.isNull(_cursorIndexOfImageDescription_1)) {
              _tmpImageDescription_1 = null;
            } else {
              _tmpImageDescription_1 = _cursor.getString(_cursorIndexOfImageDescription_1);
            }
            _item = new ImageData(_tmpId,_tmpImageUri,_tmpImageTitle,_tmpImageDescription,_tmpThumbnailUri,_tmpPosition);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public ImageData getItem(final int id) {
    final String _sql = "SELECT * from image WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
      final int _cursorIndexOfImageTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfImageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfThumbnailUri = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUri");
      final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
      final ImageData _result;
      if(_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpImageUri;
        if (_cursor.isNull(_cursorIndexOfImageUri)) {
          _tmpImageUri = null;
        } else {
          _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
        }
        final String _tmpImageTitle;
        if (_cursor.isNull(_cursorIndexOfImageTitle)) {
          _tmpImageTitle = null;
        } else {
          _tmpImageTitle = _cursor.getString(_cursorIndexOfImageTitle);
        }
        final String _tmpImageDescription;
        if (_cursor.isNull(_cursorIndexOfImageDescription)) {
          _tmpImageDescription = null;
        } else {
          _tmpImageDescription = _cursor.getString(_cursorIndexOfImageDescription);
        }
        final String _tmpThumbnailUri;
        if (_cursor.isNull(_cursorIndexOfThumbnailUri)) {
          _tmpThumbnailUri = null;
        } else {
          _tmpThumbnailUri = _cursor.getString(_cursorIndexOfThumbnailUri);
        }
        final String _tmpPosition;
        if (_cursor.isNull(_cursorIndexOfPosition)) {
          _tmpPosition = null;
        } else {
          _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
        }
        _result = new ImageData(_tmpId,_tmpImageUri,_tmpImageTitle,_tmpImageDescription,_tmpThumbnailUri,_tmpPosition);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
