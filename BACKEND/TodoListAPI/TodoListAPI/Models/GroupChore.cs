using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System.Text.Json.Serialization;

namespace TodoListAPI.Models
{
    public class GroupChore
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        [JsonPropertyName("id")]
        public string Id { get; set; }

        [JsonPropertyName("name")]
        public string Name { get; set; }

        [JsonPropertyName("access_token")]
        public string Token { get; set; }

        [JsonPropertyName("tasks")]
        public List<Chore> Chores { get; set; }

        public GroupChore()
        {
            Chores = new();
        }
    }
}
