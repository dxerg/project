package com.example.project.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MatrixFactorizationRecommendation {
    // MySQL数据库连接信息
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_videoplayback_sys?serverTimezone=GMT%2b8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "114514";

    private final int numUsers; // 用户数量
    private final int numItems; // 物品数量
    private final int numFactors; // 因子数量
    private final double learningRate; // 学习率
    private final double regularization; // 正则化参数
    private final double biasLearningRate; // 偏置学习率
    private final double biasRegularization; // 偏置正则化参数
    private final int numIterations; // 迭代次数

    // 数据存储
    private final Map<Integer, Map<Integer, Double>> userRatings;
    private final Map<Integer, Map<Integer, Double>> itemRatings;
    private final Map<Integer, Double> userBiases;
    private final Map<Integer, Double> itemBiases;
    private final double globalBias;
    private final double[][] userFeatures;
    private final double[][] itemFeatures;

    public MatrixFactorizationRecommendation(int numUsers, int numItems, int numFactors, double learningRate,
                                double regularization, double biasLearningRate, double biasRegularization,
                                int numIterations) {
        this.numUsers = numUsers;
        this.numItems = numItems;
        this.numFactors = numFactors;
        this.learningRate = learningRate;
        this.regularization = regularization;
        this.biasLearningRate = biasLearningRate;
        this.biasRegularization = biasRegularization;
        this.numIterations = numIterations;

        this.userRatings = new HashMap<>();
        this.itemRatings = new HashMap<>();
        this.userBiases = new HashMap<>();
        this.itemBiases = new HashMap<>();
        this.globalBias = 0.0;
        this.userFeatures = new double[numUsers][numFactors];
        this.itemFeatures = new double[numItems][numFactors];
    }

    // 从数据库加载用户-物品评分数据
    public void loadDataFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT user_id, item_id, rating FROM ratings";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int userId = rs.getInt("user_id") - 1; // 调整为0-based索引
                    int itemId = rs.getInt("item_id") - 1; // 调整为0-based索引
                    double rating = rs.getDouble("rating");

                    userRatings.computeIfAbsent(userId, k -> new HashMap<>()).put(itemId, rating);
                    itemRatings.computeIfAbsent(itemId, k -> new HashMap<>()).put(userId, rating);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SVD++训练过程
    public void train() {
        initializeFeatures(); // 初始化特征

        for (int iter = 0; iter < numIterations; iter++) {
            for (int userId = 0; userId < numUsers; userId++) {
                for (int itemId = 0; itemId < numItems; itemId++) {
                    if (userRatings.containsKey(userId) && userRatings.get(userId).containsKey(itemId)) {
                        double prediction = predict(userId, itemId);
                        double error = userRatings.get(userId).get(itemId) - prediction;

                        // 更新用户和物品偏置
                        double userBias = userBiases.get(userId);
                        double itemBias = itemBiases.get(itemId);
                        double userBiasGradient = error - biasRegularization * userBias;
                        double itemBiasGradient = error - biasRegularization * itemBias;
                        userBiases.put(userId, userBias + biasLearningRate * userBiasGradient);
                        itemBiases.put(itemId, itemBias + biasLearningRate * itemBiasGradient);

                        // 更新用户和物品特征向量
                        for (int factor = 0; factor < numFactors; factor++) {
                            double userFeature = userFeatures[userId][factor];
                            double itemFeature = itemFeatures[itemId][factor];
                            double userFeatureGradient = error * itemFeature - regularization * userFeature;
                            double itemFeatureGradient = error * (userFeature + itemFeature) - regularization * itemFeature;
                            userFeatures[userId][factor] += learningRate * userFeatureGradient;
                            itemFeatures[itemId][factor] += learningRate * itemFeatureGradient;
                        }
                    }
                }
            }
        }
    }

    // 预测用户对物品的评分
    public double predict(int userId, int itemId) {
        double prediction = globalBias + userBiases.get(userId) + itemBiases.get(itemId);

        for (int factor = 0; factor < numFactors; factor++) {
            prediction += userFeatures[userId][factor] * itemFeatures[itemId][factor];
        }

        return prediction;
    }

    // 初始化特征
    private void initializeFeatures() {
        Random random = new Random();
        for (int userId = 0; userId < numUsers; userId++) {
            userBiases.put(userId, 0.0);
            for (int factor = 0; factor < numFactors; factor++) {
                userFeatures[userId][factor] = random.nextDouble() * 0.1;
            }
        }

        for (int itemId = 0; itemId < numItems; itemId++) {
            itemBiases.put(itemId, 0.0);
            for (int factor = 0; factor < numFactors; factor++) {
                itemFeatures[itemId][factor] = random.nextDouble() * 0.1;
            }
        }
    }

    public static void repredict() {
        int numUsers = 100;
        int numItems = 50;
        int numFactors = 10;
        double learningRate = 0.01;
        double regularization = 0.05;
        double biasLearningRate = 0.005;
        double biasRegularization = 0.02;
        int numIterations = 100;

        MatrixFactorizationRecommendation svdPlusPlus = new MatrixFactorizationRecommendation(numUsers, numItems, numFactors, learningRate,
                regularization, biasLearningRate, biasRegularization, numIterations);
        svdPlusPlus.loadDataFromDatabase(); // 从数据库加载数据
        svdPlusPlus.train(); // 训练模型
        // 进行预测
    }


}
