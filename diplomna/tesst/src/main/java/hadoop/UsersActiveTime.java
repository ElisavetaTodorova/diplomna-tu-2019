package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class UsersActiveTime {


  public static class TokenizerMapper
      extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
    ) throws IOException, InterruptedException {
      // The user with id '7160'

      String[] split = value.toString().split(",");
      
      String userActiveTime = split[0];
      context.write(new Text(userActiveTime), new IntWritable(1));

    }

  }

  public static class IntSumReducer
      extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
    ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }


  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Users Active time");
    job.setJarByClass(UsersActiveTime.class);
    job.setMapperClass(UsersActiveTime.TokenizerMapper.class);
    job.setCombinerClass(UsersActiveTime.IntSumReducer.class);
    job.setReducerClass(UsersActiveTime.IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path("/Users/i338442/diplomna/tesst/src/main/resources/input"));
    FileOutputFormat.setOutputPath(job, new Path("/Users/i338442/diplomna/tesst/src/main/resources/usersActiveTime"));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
