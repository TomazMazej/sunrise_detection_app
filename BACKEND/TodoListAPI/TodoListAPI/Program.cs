using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using TodoListAPI.Services;
using TodoListAPI.Models;

namespace TodoListAPI
{
    public class Program
    {
        public static void Main(string[] args)
        {
            BookstoreDatabaseSettings settings = new BookstoreDatabaseSettings();
            settings.ConnectionString = "mongodb+srv://MKFJM_Development:KVJwkNEZLjHxAopH@cluster0.rn5pq.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
            settings.DatabaseName = "MKFJM_Development";
            settings.BooksCollectionName = "TODO_List";
            BookService neke = new BookService(settings);
            List<Book> knjige = neke.Get();
            foreach (Book knjiga in knjige)
            {
                Console.WriteLine(knjiga.BookName);
            }

            CreateHostBuilder(args).Build().Run();

        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder.UseStartup<Startup>();
                });
    }
}
