using TodoListAPI.Models;
using TodoListAPI.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace BooksApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ChoreController : ControllerBase
    {
        private readonly GroupChoreService _service;

        public ChoreController(GroupChoreService service)
        {
            _service = service;
        }

        [HttpGet("{id:length(24)}")]
        public ActionResult<List<Chore>> Get(string id)
        {
            Request.Headers.TryGetValue("Authorization", out var token);

            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }
            else if (groupChore.Token != token)
            {
                return Unauthorized();
            }

            return groupChore.Chores;
        }

        [HttpGet("{id:length(24)}/{choreId:length(24)}", Name = "GetChore")]
        public ActionResult<Chore> GetChore(string id, string choreId)
        {
            Request.Headers.TryGetValue("Authorization", out var token);

            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }

            else if (groupChore.Token != token)
            {
                return Unauthorized();
            }

            return groupChore.Chores.Find(x => x.Id == choreId);
        }

        [HttpPost("{id:length(24)}")]
        public ActionResult<Chore> CreateChore(string id, Chore chore)
        {
            var groupChore = _service.Get(id);
            groupChore.Chores.Add(chore);
            chore.AssignId();
            _service.Update(id, groupChore);

            return CreatedAtRoute("GetChore", new { id = id, choreId = chore.Id.ToString() }, chore);
        }

        [HttpPut("{id:length(24)}/{choreId:length(24)}")]
        public IActionResult UpdateChore(string id, string choreId, Chore choreIn)
        {
            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }

            var chore = groupChore.Chores.Find(x => x.Id == choreId);
            if(chore == null)
            {
                return NotFound();
            }

            groupChore.Chores.Remove(chore);
            groupChore.Chores.Add(choreIn);

            _service.Update(id, groupChore);

            return NoContent();
        }

        [HttpDelete("{id:length(24)}/{choreId:length(24)}")]
        public IActionResult DeleteChore(string id, string choreId)
        {
            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }

            var chore = groupChore.Chores.Find(x => x.Id == choreId);
            if (chore == null)
            {
                return NotFound();
            }

            groupChore.Chores.Remove(chore);

            _service.Update(id, groupChore);

            return NoContent();
        }
    }
}

