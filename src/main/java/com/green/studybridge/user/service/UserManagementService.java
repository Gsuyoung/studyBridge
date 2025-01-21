package com.green.studybridge.user.service;

import com.green.studybridge.config.MyFileUtils;
import com.green.studybridge.config.constant.UserConst;
import com.green.studybridge.config.exception.CommonErrorCode;
import com.green.studybridge.config.exception.CustomException;
import com.green.studybridge.config.exception.UserErrorCode;
import com.green.studybridge.config.security.AuthenticationFacade;
import com.green.studybridge.user.UserUtils;
import com.green.studybridge.user.entity.User;
import com.green.studybridge.user.model.UserDeleteReq;
import com.green.studybridge.user.model.UserSignInRes;
import com.green.studybridge.user.model.UserUpdateReq;
import com.green.studybridge.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpUserCache signUpUserCache;
    private final UserUtils userUtils;
    private final MyFileUtils myFileUtils;
    private final UserConst userConst;

    @Transactional
    public UserSignInRes signUp(String token, HttpServletResponse response) {
        User user = signUpUserCache.verifyToken(token);
        userRepository.save(user);

        return userUtils.generateUserSignInResByUser(user, response);
    }

    public void updateUserPic(MultipartFile pic) {
        long userId = AuthenticationFacade.getSignedUserId();
        User user = userUtils.getUserById(userId);

        String prePic = user.getUserPic();
        user.setUserPic(myFileUtils.makeRandomFileName(pic));
        userRepository.save(user);

        String folderPath = String.format(userConst.getUserPicFilePath(), userId);
        if (prePic != null) {
            myFileUtils.deleteFolder(folderPath, false);
        }
        myFileUtils.makeFolders(folderPath);
        String filePath = String.format("%s/%s", folderPath, user.getUserPic());
        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateUser(UserUpdateReq req) {
        User user = userUtils.getUserById(AuthenticationFacade.getSignedUserId());
        user.setName(req.getName());
        user.setNickName(req.getNickName());
        user.setBirth(req.getBirth());
        user.setPhone(req.getPhone());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(@Valid UserDeleteReq req) {
        long userId = AuthenticationFacade.getSignedUserId();
        User user = userUtils.getUserById(userId);
        if (!passwordEncoder.matches(req.getPw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_PW);
        }
        userRepository.deleteById(userId);
        String folderPath = String.format(userConst.getUserPicFilePath(), userId);
        myFileUtils.deleteFolder(folderPath, true);
    }
}
