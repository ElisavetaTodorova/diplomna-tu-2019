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

public class LogsPerDate {

  private static String date;

  public static class TokenizerMapper
      extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
    ) throws IOException, InterruptedException {


      if (value.toString().contains(date)) {
        String[] split = value.toString().split(",");

        context.write(new Text(String.format("%s,%s,%s,%s,%s", split[2], split[3], split[4], split[5], split[6])), new IntWritable(1));
      }

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
    date = args[0];
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "LogsPreDate" + date);
    job.setJarByClass(LogsPerDate.class);
    job.setMapperClass(LogsPerDate.TokenizerMapper.class);
    job.setCombinerClass(LogsPerDate.IntSumReducer.class);
    job.setReducerClass(LogsPerDate.IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path("/Users/i338442/diplomna/backend/src/main/resources/input"));
    FileOutputFormat.setOutputPath(job, new Path("/Users/i338442/diplomna/backend/src/main/resources/logsPerDate" + (date.replace("/", "-"))));

    job.waitForCompletion(true);
  }
}
