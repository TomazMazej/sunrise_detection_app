using TodoListAPI.Models;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TodoListAPI.Services
{
    public class GroupChoreService
    {
        private readonly IMongoCollection<GroupChore> _collection;

        public GroupChoreService(IMongoDatabaseSettings settings)
        {
            var client = new MongoClient(settings.ConnectionString);
            var database = client.GetDatabase(settings.DatabaseName);

            _collection = database.GetCollection<GroupChore>(settings.CollectionName);
        }

        // ---------------------- ASYNC ----------------------------

        public async Task<List<GroupChore>> GetAsync() =>
            await _collection.Find(groupChore => true).ToListAsync();

        public async Task<GroupChore> GetAsync(string id) =>
            await _collection.Find(groupChore => groupChore.Id == id).FirstOrDefaultAsync();

        public async Task<GroupChore> CreateAsync(GroupChore groupChore)
        {
            await _collection.InsertOneAsync(groupChore);
            return groupChore;
        }

        public async Task UpdateAsync(string id, GroupChore groupChoreIn) =>
            await _collection.ReplaceOneAsync(groupChore => groupChore.Id == id, groupChoreIn);

        public async Task RemoveAsync(GroupChore groupChoreOut) =>
            await _collection.DeleteOneAsync(groupChore => groupChore.Id == groupChoreOut.Id);

        public async Task RemoveAsync(string id) =>
            await _collection.DeleteOneAsync(groupChore => groupChore.Id == id);


        // ---------------------- SYNC ----------------------------

        public List<GroupChore> Get() =>
            _collection.Find(groupChore => true).ToList();

        public GroupChore Get(string id) =>
            _collection.Find(groupChore => groupChore.Id == id).FirstOrDefault();

        public GroupChore Create(GroupChore groupChore)
        {
            groupChore.Id = null;
            foreach (var chore in groupChore.Chores) chore.AssignId();

            _collection.InsertOne(groupChore);
            return groupChore;
        }

        public void Update(string id, GroupChore groupChoreIn) =>
            _collection.ReplaceOne(groupChore => groupChore.Id == id, groupChoreIn);

        public void Remove(GroupChore groupChoreOut) =>
            _collection.DeleteOne(groupChore => groupChore.Id == groupChoreOut.Id);

        public void Remove(string id) =>
            _collection.DeleteOne(groupChore => groupChore.Id == id);
    }
}
