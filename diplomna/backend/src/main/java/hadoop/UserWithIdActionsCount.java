package hadoop;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserWithIdActionsCount {
  
  private static String userId;

  public static class TokenizerMapper
      extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
    ) throws IOException, InterruptedException {
      String pattern = "The user with id '" + userId + "'";

      Pattern r = Pattern.compile(pattern);
      Matcher m = r.matcher(value.toString());

      File file = new File("/Users/i338442/diplomna/backend/src/main/resources/userIdPlusDateCount" + userId + ".txt");
      file.createNewFile();

      try(
          FileWriter fw = new FileWriter("/Users/i338442/diplomna/backend/src/main/resources/userIdPlusDateCount" + userId + ".txt", true);
          BufferedWriter bw = new BufferedWriter(fw)) {

        if (m.find()) {
          String[] split = value.toString().split(",");
          
          bw.write(split[0] + "," + split[1]);
          bw.newLine();
          String userAction = split[5];
          context.write(new Text(userAction), new IntWritable(1));
        }
        
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
  
  
  public static void execute(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
    userId = args[0];
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "UserIds actions Count" + userId);
    job.setJarByClass(UserWithIdActionsCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path("/Users/i338442/diplomna/backend/src/main/resources/input"));

    FileUtils.deleteDirectory(new File("/Users/i338442/diplomna/backend/src/main/resources/outputUserIdsActionsCount" + userId));
    FileOutputFormat.setOutputPath(job, new Path("/Users/i338442/diplomna/backend/src/main/resources/outputUserIdsActionsCount" + userId));
    boolean b = job.waitForCompletion(true);
  }
}


