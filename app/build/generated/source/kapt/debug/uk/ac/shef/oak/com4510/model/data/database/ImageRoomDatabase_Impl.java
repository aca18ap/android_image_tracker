package uk.ac.shef.oak.com4510.model.data.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.FtsTableInfo;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ImageRoomDatabase_Impl extends ImageRoomDatabase {
  private volatile ImageDataDao _imageDataDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `image` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `uri` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `thumbnailUri` TEXT, `position` TEXT)");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_image_id_title` ON `image` (`id`, `title`)");
        _db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `image_fts` USING FTS4(`title` TEXT NOT NULL, `description` TEXT, content=`image`)");
        _db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_image_fts_BEFORE_UPDATE BEFORE UPDATE ON `image` BEGIN DELETE FROM `image_fts` WHERE `docid`=OLD.`rowid`; END");
        _db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_image_fts_BEFORE_DELETE BEFORE DELETE ON `image` BEGIN DELETE FROM `image_fts` WHERE `docid`=OLD.`rowid`; END");
        _db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_image_fts_AFTER_UPDATE AFTER UPDATE ON `image` BEGIN INSERT INTO `image_fts`(`docid`, `title`, `description`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`description`); END");
        _db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_image_fts_AFTER_INSERT AFTER INSERT ON `image` BEGIN INSERT INTO `image_fts`(`docid`, `title`, `description`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`description`); END");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2f37c61037e8891e3a4afcd6cc09c686')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `image`");
        _db.execSQL("DROP TABLE IF EXISTS `image_fts`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_image_fts_BEFORE_UPDATE BEFORE UPDATE ON `image` BEGIN DELETE FROM `image_fts` WHERE `docid`=OLD.`rowid`; END");
        _db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_image_fts_BEFORE_DELETE BEFORE DELETE ON `image` BEGIN DELETE FROM `image_fts` WHERE `docid`=OLD.`rowid`; END");
        _db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_image_fts_AFTER_UPDATE AFTER UPDATE ON `image` BEGIN INSERT INTO `image_fts`(`docid`, `title`, `description`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`description`); END");
        _db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_image_fts_AFTER_INSERT AFTER INSERT ON `image` BEGIN INSERT INTO `image_fts`(`docid`, `title`, `description`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`description`); END");
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsImage = new HashMap<String, TableInfo.Column>(6);
        _columnsImage.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImage.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImage.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImage.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImage.put("thumbnailUri", new TableInfo.Column("thumbnailUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImage.put("position", new TableInfo.Column("position", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysImage = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesImage = new HashSet<TableInfo.Index>(1);
        _indicesImage.add(new TableInfo.Index("index_image_id_title", false, Arrays.asList("id","title")));
        final TableInfo _infoImage = new TableInfo("image", _columnsImage, _foreignKeysImage, _indicesImage);
        final TableInfo _existingImage = TableInfo.read(_db, "image");
        if (! _infoImage.equals(_existingImage)) {
          return new RoomOpenHelper.ValidationResult(false, "image(uk.ac.shef.oak.com4510.model.data.database.ImageData).\n"
                  + " Expected:\n" + _infoImage + "\n"
                  + " Found:\n" + _existingImage);
        }
        final HashSet<String> _columnsImageFts = new HashSet<String>(2);
        _columnsImageFts.add("title");
        _columnsImageFts.add("description");
        final FtsTableInfo _infoImageFts = new FtsTableInfo("image_fts", _columnsImageFts, "CREATE VIRTUAL TABLE IF NOT EXISTS `image_fts` USING FTS4(`title` TEXT NOT NULL, `description` TEXT, content=`image`)");
        final FtsTableInfo _existingImageFts = FtsTableInfo.read(_db, "image_fts");
        if (!_infoImageFts.equals(_existingImageFts)) {
          return new RoomOpenHelper.ValidationResult(false, "image_fts(uk.ac.shef.oak.com4510.model.data.database.ImageDataFTS).\n"
                  + " Expected:\n" + _infoImageFts + "\n"
                  + " Found:\n" + _existingImageFts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2f37c61037e8891e3a4afcd6cc09c686", "7afafc7ad96679659d481f63354a0878");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(1);
    _shadowTablesMap.put("image_fts", "image");
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "image","image_fts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `image`");
      _db.execSQL("DELETE FROM `image_fts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ImageDataDao.class, ImageDataDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public ImageDataDao imageDataDao() {
    if (_imageDataDao != null) {
      return _imageDataDao;
    } else {
      synchronized(this) {
        if(_imageDataDao == null) {
          _imageDataDao = new ImageDataDao_Impl(this);
        }
        return _imageDataDao;
      }
    }
  }
}
