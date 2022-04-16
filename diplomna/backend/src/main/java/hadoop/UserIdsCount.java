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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserIdsCount {
  public static class TokenizerMapper
      extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
    ) throws IOException, InterruptedException {
      String pattern = "The user with id '([0-9]*)'";

      Pattern r = Pattern.compile(pattern);
      Matcher m = r.matcher(value.toString());
      
      if (m.find()) {
        String group = m.group(1);
        context.write(new Text(group), new IntWritable(1));
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
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "UserIds Count");
    job.setJarByClass(UserIdsCount.class);
    job.setMapperClass(UserIdsCount.TokenizerMapper.class);
    job.setCombinerClass(UserIdsCount.IntSumReducer.class);
    job.setReducerClass(UserIdsCount.IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path("/Users/i338442/diplomna/backend/src/main/resources/input"));
    FileOutputFormat.setOutputPath(job, new Path("/Users/i338442/diplomna/backend/src/main/resources/outputUserIds"));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
